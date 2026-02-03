package com.app.finance.models.requests

import com.app.finance.enums.Category
import java.math.BigDecimal

data class AccountRequestDTO(
    val category: Category? = null,
    val initialBalance: BigDecimal? = null,
)
