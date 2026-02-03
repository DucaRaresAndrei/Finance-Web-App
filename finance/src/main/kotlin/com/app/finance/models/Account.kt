package com.app.finance.models

import com.app.finance.enums.Category
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "accounts")
data class Account (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "card_id")
    val cardId: String? = null,

    //val history: History? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val category: Category = Category.USER,

    @Column(unique = true, nullable = false, length = 24)
    val iban: String,

    @Column(nullable = false, precision = 19, scale = 2)
    var balance: BigDecimal,

    // lista de prieteni
    @ElementCollection
    @CollectionTable(
        name = "account_friends",
        joinColumns = [JoinColumn(name = "account_id")]
    )
    @Column(name = "friend_account_id")
    val friends: List<Long> = emptyList(),

    @Column(name = "low_balance_alert_enabled")
    var lowBalanceAlertEnabled: Boolean = true,

    @Column(name = "low_balance_threshold", precision = 19, scale = 2)
    var lowBalanceThreshold: BigDecimal = BigDecimal("150"),

    val lastLowBalanceAlertAt: LocalDateTime? = null
)
