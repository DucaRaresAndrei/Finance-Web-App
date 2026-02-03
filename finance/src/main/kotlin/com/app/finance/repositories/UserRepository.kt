package com.app.finance.repositories

import com.app.finance.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Optional<User>
    fun findByAccountId(accountId: Long?): User
    fun findByAccountIdIn(accountIds: List<Long>): List<User>

    fun findByKeycloakId(keycloakId: String): Optional<User>
}
