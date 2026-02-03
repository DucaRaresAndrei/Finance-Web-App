package com.app.finance.models.responses

import com.app.finance.enums.Category

data class FirmDTO(
    val fullName: String, // fullName
    val email: String,
    val category: Category,
    val iban: String
)
