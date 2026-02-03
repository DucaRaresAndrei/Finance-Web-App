package com.app.finance.repositories

import com.app.finance.models.Income
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface IncomeRepository : JpaRepository<Income, String> {
    fun countByIbanReceiver(iban: String): Long
    fun findTop5ByIbanReceiverOrderByDateDesc(iban: String): List<Income>
    fun findByIbanReceiverAndDateBetween(iban: String, start: LocalDateTime, end: LocalDateTime) : List<Income>
    fun findByIbanReceiverOrderByDateDesc(iban: String): List<Income>
}