package com.app.finance.controllers

import com.app.finance.models.requests.LoginRequestDTO
import com.app.finance.models.requests.RegisterRequestDTO
import com.app.finance.services.KeycloakAuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val keycloakAuthService: KeycloakAuthService,
) {

    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequestDTO): ResponseEntity<Void> {
        keycloakAuthService.registerUser(req)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequestDTO): ResponseEntity<Map<String, Any?>> {
        val tokens = keycloakAuthService.login(req)
        return ResponseEntity.ok(tokens)
    }

    @PostMapping("/logout")
    fun logout(@RequestBody body: Map<String, String>): ResponseEntity<Void> {
        val refreshToken = body["refresh_token"]
            ?: return ResponseEntity.badRequest().build()

        keycloakAuthService.logout(refreshToken)
        return ResponseEntity.noContent().build()
    }
}
