package com.app.notification.dto

import java.math.BigDecimal

data class AccountCreatedReq(
    val to: String,
    val fullName: String
)

data class TransferSuccessReq(
    val to: String,
    val myFullName: String,
    val amount: BigDecimal,
    val destinationIban: String,
    val destinationName: String?,
    val currentBalance: BigDecimal
)

data class FirmPaymentSuccessReq(
    val to: String,
    val myFullName: String,
    val amount: BigDecimal,
    val firmIban: String,
    val firmName: String?,
    val currentBalance: BigDecimal
)

data class FriendAddedReq(
    val to: String,
    val ownerFullName: String,
    val friendFullName: String?,
    val friendIban: String
)

data class SettingsChangedReq(
    val to: String,
    val fullName: String
)
