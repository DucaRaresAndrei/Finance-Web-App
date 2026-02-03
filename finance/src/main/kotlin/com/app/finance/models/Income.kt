package com.app.finance.models

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "incomes")
data class Income(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,

    @Column(nullable = false, precision = 19, scale = 2)
    val amount: BigDecimal,
    val description: String,
    val date: LocalDateTime = LocalDateTime.now(),
    val ibanSender: String,
    val ibanReceiver: String
)
