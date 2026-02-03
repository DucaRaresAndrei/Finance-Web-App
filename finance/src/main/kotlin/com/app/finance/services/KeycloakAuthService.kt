package com.app.finance.services

import com.app.finance.models.requests.LoginRequestDTO
import com.app.finance.models.requests.RegisterRequestDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException

@Service
class KeycloakAuthService(
    @Value("\${keycloak.server-url}") private val serverUrl: String,
    @Value("\${keycloak.realm}") private val realm: String,
    @Value("\${keycloak.client-id}") private val clientId: String,
    @Value("\${keycloak.admin.username}") private val adminUsername: String,
    @Value("\${keycloak.admin.password}") private val adminPassword: String,
    private val userService: UserService,
    private val emailService: EmailNotificationService
) {

    private val restTemplate = RestTemplate()

    // pentru API-ul de admin din keycloak
    private fun getAdminToken(): String {
        val url = "$serverUrl/realms/master/protocol/openid-connect/token"

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val body = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "password")
            add("client_id", "admin-cli")
            add("username", adminUsername)
            add("password", adminPassword)
        }

        val request = HttpEntity(body, headers)
        val response = restTemplate.postForEntity(url, request, Map::class.java)

        val token = response.body?.get("access_token") as? String
        return token ?: throw IllegalStateException("Could not obtain admin token from Keycloak")
    }

    // creeaza userul in Keycloak
    fun registerUser(req: RegisterRequestDTO) {
        val adminToken = getAdminToken()

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            setBearerAuth(adminToken)
        }

        // cream userul
        val createUrl = "$serverUrl/admin/realms/$realm/users"

        val payload = mapOf(
            "username" to req.email,
            "email" to req.email,
            "enabled" to true,
            "emailVerified" to true,
            "requiredActions" to emptyList<String>(),
            "firstName" to req.fullName,
            "lastName" to req.fullName,
            "attributes" to mapOf(
                "fullName" to listOf(req.fullName)
            ),
            "credentials" to listOf(
                mapOf(
                    "type" to "password",
                    "value" to req.password,
                    "temporary" to false
                )
            )
        )

        try {
            val response = restTemplate.postForEntity(createUrl, HttpEntity(payload, headers), Void::class.java)

            val location = response.headers.location
                ?: throw IllegalStateException("User created but Location header is missing")
            val keycloakId = location.path.substringAfterLast("/")

            // configurare user
            val userUrl = "$serverUrl/admin/realms/$realm/users/$keycloakId"

            val getUserResponse = restTemplate.exchange(
                userUrl,
                HttpMethod.GET,
                HttpEntity<Void>(headers),
                Map::class.java
            )

            @Suppress("UNCHECKED_CAST")
            val userRep = (getUserResponse.body as? MutableMap<String, Any?>)
                ?: throw IllegalStateException("Cannot read user representation from Keycloak")

            userRep["firstName"] = req.fullName
            userRep["lastName"] = req.fullName
            userRep["emailVerified"] = true
            userRep["requiredActions"] = emptyList<String>()

            // il punem inapoi in keycloak
            restTemplate.exchange(
                userUrl,
                HttpMethod.PUT,
                HttpEntity(userRep, headers),
                Void::class.java
            )

            // default USER
            assignRealmRole(keycloakId, "USER", adminToken)

            // il adaugam in baza de date
            userService.ensureUserFromRegister(
                keycloakId = keycloakId,
                email = req.email,
                fullName = req.fullName
            )

            // trimitem mail - am creat un nou cont
            emailService.sendAccountCreatedEmail(req.email, req.fullName)

        } catch (ex: HttpClientErrorException.Conflict) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists")
        }
    }


    // ataseaza rolul userului
    private fun assignRealmRole(userId: String, roleName: String, adminToken: String) {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            setBearerAuth(adminToken)
        }

        // luam rolul
        val roleUrl = "$serverUrl/admin/realms/$realm/roles/$roleName"
        val roleResponse = restTemplate.exchange(
            roleUrl,
            HttpMethod.GET,
            HttpEntity<Void>(headers),
            Map::class.java
        )
        val roleRep = roleResponse.body ?: return

        // il atasam la user
        val mappingUrl = "$serverUrl/admin/realms/$realm/users/$userId/role-mappings/realm"
        restTemplate.exchange(
            mappingUrl,
            HttpMethod.POST,
            HttpEntity(listOf(roleRep), headers),
            Void::class.java
        )
    }

    // login prin care obtinem token-ul de acces + de refresh
    fun login(req: LoginRequestDTO): Map<String, Any?> {
        val url = "$serverUrl/realms/$realm/protocol/openid-connect/token"

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val body = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "password")
            add("client_id", clientId)
            add("username", req.username)
            add("password", req.password)
        }

        val response = restTemplate.postForEntity(url, HttpEntity(body, headers), Map::class.java)

        if (!response.statusCode.is2xxSuccessful) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }

        @Suppress("UNCHECKED_CAST")
        return response.body as? Map<String, Any?> ?: emptyMap()
    }

    fun logout(refreshToken: String) {
        val url = "$serverUrl/realms/$realm/protocol/openid-connect/logout"

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val body = LinkedMultiValueMap<String, String>().apply {
            add("client_id", clientId)
            add("refresh_token", refreshToken)
        }

        restTemplate.postForEntity(url, HttpEntity(body, headers), Void::class.java)
    }
}
