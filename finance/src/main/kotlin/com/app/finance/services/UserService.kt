package com.app.finance.services

import com.app.finance.controllers.AdminUserRowDTO
import com.app.finance.enums.Role
import com.app.finance.mappers.UserMapper
import com.app.finance.models.User
import com.app.finance.models.requests.AccountRequestDTO
import com.app.finance.models.responses.UserResponseDTO
import com.app.finance.repositories.AccountRepository
import com.app.finance.repositories.UserRepository
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val accountService: AccountService,
    private val cardService: CardService,
    private val accountRepository: AccountRepository,
    private val currentUser: CurrentUserService
) {

    // extrag datele despre userul curent
    @Transactional
    fun ensureUserFromKeycloak(jwt: Jwt): UserResponseDTO {
        val keycloakId = jwt.subject
        val email = jwt.getClaim<String>("email")
        val name = jwt.getClaim<String>("name") ?: email.substringBefore("@")

        val realmAccess = jwt.getClaim<Map<String, Any>>("realm_access") ?: emptyMap()
        val roles = (realmAccess["roles"] as? Collection<*>)?.filterIsInstance<String>() ?: emptyList()
        val isAdmin = "ADMIN" in roles

        return ensureUserInternal(
            keycloakId = keycloakId,
            email = email,
            name = name,
            isAdmin = isAdmin
        )
    }

    // inregistrez userul si in baza de date, dupa ce s-a creat in Keycloak
    @Transactional
    fun ensureUserFromRegister(
        keycloakId: String,
        email: String,
        fullName: String
    ): UserResponseDTO {
        return ensureUserInternal(
            keycloakId = keycloakId,
            email = email,
            name = fullName,
            isAdmin = false
        )
    }

    // logica comuna:
    // - daca userul nu exista, il creeaza in Postgre
    // - il sincornizeaza si il intoarce
    private fun ensureUserInternal(
        keycloakId: String,
        email: String,
        name: String,
        isAdmin: Boolean
    ): UserResponseDTO {
        var user: User? = userRepository.findByKeycloakId(keycloakId).orElse(null)

        if (user == null) {
            // cream cont + card la register
            val account = accountService.createAccount(AccountRequestDTO())
            val card = cardService.createCard()
            val updatedAccount = account.copy(cardId = card.id)
            val savedAcc = accountRepository.save(updatedAccount)

            user = User(
                keycloakId = keycloakId,
                email = email,
                fullName = name,
                accountId = savedAcc.id,
                role = if (isAdmin) Role.ADMIN else Role.USER
            )
            user = userRepository.save(user)
        } else {
            // sincronizam rolul daca s-a schimbat in Keycloak
            val newRole = if (isAdmin) Role.ADMIN else Role.USER
            if (user.role != newRole) {
                user = user.copy(role = newRole)
                user = userRepository.save(user)
            }
        }

        val account = accountRepository.findById(user.accountId!!).orElseThrow()
        val card = cardService.getCardForAccount(account)
        val history = accountService.getHistorySummary(account.iban)

        return UserMapper.toResponseDTO(user, account, card, history)
    }

    fun getUser(): UserResponseDTO {
        val account = currentUser.getLoggedAccount()
        val user = userRepository.findByAccountId(account.id)
        val history = accountService.getHistorySummary(account.iban)
        val card = cardService.getCardForAccount(account)
        return UserMapper.toResponseDTO(user, account, card, history)
    }

    fun getAllUsers(): List<AdminUserRowDTO> {
        //val users = userRepository.findAll()
        val accounts = accountRepository.findAll()

        if (accounts.isEmpty()) return emptyList()

        val accountIds = accounts.mapNotNull { it.id }
        val usersByAccountId = userRepository.findByAccountIdIn(accountIds).associateBy { it.accountId }

        return accounts.mapNotNull { acc ->
            val owner = usersByAccountId[acc.id] ?: return@mapNotNull null
            if (owner.role == Role.ADMIN) return@mapNotNull null

            AdminUserRowDTO(
                username = owner.fullName,
                email = owner.email,
                category = acc.category,
            )
        }
    }
}
