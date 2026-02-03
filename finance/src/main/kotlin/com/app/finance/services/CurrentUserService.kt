package com.app.finance.services

import com.app.finance.models.Account
import com.app.finance.models.User
import com.app.finance.repositories.AccountRepository
import com.app.finance.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CurrentUserService(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) {

    fun getLoggedUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in")

        val keycloakId = authentication.name  // by default 'sub'
        return userRepository.findByKeycloakId(keycloakId)
            .orElseThrow { ResponseStatusException(HttpStatus.UNAUTHORIZED, "User profile not found") }
    }

    fun getLoggedAccount(): Account {
        val user = getLoggedUser()
        val accountId = user.accountId
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User has no account")
        return accountRepository.findById(accountId)
            .orElseThrow { ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not found") }
    }
}
