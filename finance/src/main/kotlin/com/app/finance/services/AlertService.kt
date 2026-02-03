package com.app.finance.services

import com.app.finance.models.responses.LowBalanceAlertDTO
import com.app.finance.repositories.AccountRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AlertService(
    private val accountRepository: AccountRepository,
    private val currentUser: CurrentUserService

) {

    fun checkLowBalance(): LowBalanceAlertDTO {
        val account = currentUser.getLoggedAccount()

        if (!account.lowBalanceAlertEnabled) {
            return LowBalanceAlertDTO(shouldAlert = false)
        }

        val balance = account.balance
        val threshold = account.lowBalanceThreshold

        if (balance < threshold) {
            val message = buildString {
                append("Your balance dropped below ")
                append(threshold.stripTrailingZeros().toPlainString())
                append(" RON. Current balance: ")
                append(balance.stripTrailingZeros().toPlainString())
                append(" RON.")
            }

            val now = LocalDateTime.now()
            if (account.lastLowBalanceAlertAt == null || account.lastLowBalanceAlertAt.isBefore(now.minusMinutes(5))) {
                accountRepository.save(account.copy(lastLowBalanceAlertAt = now))
            }

            return LowBalanceAlertDTO(
                shouldAlert = true,
                message = message,
                balance = balance
            )
        }

        return LowBalanceAlertDTO(shouldAlert = false)
    }
}