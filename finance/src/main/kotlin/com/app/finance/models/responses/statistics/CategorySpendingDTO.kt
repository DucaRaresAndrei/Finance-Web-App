package com.app.finance.models.responses.statistics

import com.app.finance.enums.Category
import java.math.BigDecimal

data class CategorySpendingDTO(
    val category: Category,
    val total: BigDecimal
)
