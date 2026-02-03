package com.app.finance.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "cards")
data class Card(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,

    val expirationDate: LocalDateTime,
    val cardNumber: String,
    val cvvNumber: String,
    var pinCode: String? = null
)
