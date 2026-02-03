package com.app.finance.models.responses

import java.math.BigDecimal

data class SettingsRespDTO(
    val lowBalanceEnabled: Boolean,
    val lowBalanceThreshold: BigDecimal,
    val hasPinSet: Boolean
)
