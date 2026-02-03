package com.app.finance.models.responses

import com.app.finance.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionDTO(
    val type: TransactionType,
    val amount: BigDecimal,
    val description: String? = null,
    val ibanSender: String? = null,
    val ibanReceiver: String? = null,
    val date: LocalDateTime,
    val fromOrTo: String // account username
)
