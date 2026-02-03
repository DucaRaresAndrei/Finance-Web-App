package com.app.finance.repositories

import com.app.finance.enums.Category
import com.app.finance.models.Account
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    fun existsByIban(iban: String): Boolean
    fun findByIban(iban: String): Account?
    //fun findById(accountId: String?): Optional<Account>
    fun findByCategory(category: Category): List<Account>
    fun findByCategoryIsNot(category: Category): List<Account>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :id")
    fun findByIdForUpdate(@Param("id") id: Long): Account?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.iban = :iban")
    fun findByIbanForUpdate(@Param("iban") iban: String): Account?
}