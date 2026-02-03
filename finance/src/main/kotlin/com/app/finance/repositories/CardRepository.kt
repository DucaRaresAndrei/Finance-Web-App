package com.app.finance.repositories

import com.app.finance.models.Card
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CardRepository : JpaRepository<Card, String> {
    fun findById(cardId: String?): Card?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Card c where c.id = :id")
    fun findByIdForUpdate(@Param("id") id: String): Card?
}