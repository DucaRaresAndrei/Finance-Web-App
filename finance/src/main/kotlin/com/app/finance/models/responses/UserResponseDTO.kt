package com.app.finance.models.responses

data class UserResponseDTO (
    val fullName: String,
    val email: String,
    val account: AccountResponseDTO? = null
)