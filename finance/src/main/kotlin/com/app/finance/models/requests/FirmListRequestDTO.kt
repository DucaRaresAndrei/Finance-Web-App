package com.app.finance.models.requests

import com.app.finance.enums.Category

data class FirmListRequestDTO(
    val category: Category? = null
)
