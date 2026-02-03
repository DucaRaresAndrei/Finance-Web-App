package com.app.finance.models

import com.app.finance.enums.Category
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "expenses")
data class Expense(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,

    var description: String,
    @Column(nullable = false, precision = 19, scale = 2)
    var amount: BigDecimal,
    var date: LocalDateTime = LocalDateTime.now(),
    var ibanReceiver: String,
    var ibanSender: String,

    @Enumerated(EnumType.STRING)
    var category: Category
)
