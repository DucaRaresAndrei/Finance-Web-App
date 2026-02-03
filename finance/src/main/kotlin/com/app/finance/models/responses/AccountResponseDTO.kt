package com.app.finance.models.responses

import com.app.finance.enums.Category
import java.math.BigDecimal

data class AccountResponseDTO(
    val card: CardResponseDTO? = null,
    val history: HistorySummaryDTO? = null,
    val category: Category,
    val iban: String,
    val balance: BigDecimal
)
