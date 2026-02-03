package com.app.finance.models.requests

data class RegisterRequestDTO(
    val email: String,
    val fullName: String,
    val password: String
)

data class LoginRequestDTO(
    val username: String,
    val password: String
)
