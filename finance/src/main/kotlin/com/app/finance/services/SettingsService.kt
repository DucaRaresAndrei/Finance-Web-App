package com.app.finance.services

import com.app.finance.models.requests.UpdateSettingsReqDTO
import com.app.finance.models.responses.SettingsRespDTO
import com.app.finance.repositories.AccountRepository
import com.app.finance.repositories.CardRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.math.BigDecimal
import org.springframework.transaction.annotation.Transactional

@Service
class SettingsService(
    private val accountRepository: AccountRepository,
    private val cardRepository: CardRepository,
    private val currentUser: CurrentUserService,
    private val emailService: EmailNotificationService

) {

    fun getSettings(): SettingsRespDTO {
        val account = currentUser.getLoggedAccount()
        val card = account.cardId?.let { cardRepository.findById(it).orElse(null) }
            ?: throw IllegalArgumentException("No card associated with this account!")

        val enabled = account.lowBalanceAlertEnabled ?: false
        val threshold = account.lowBalanceThreshold ?: BigDecimal("150.00")
        val hasPin = card.pinCode?.isNotBlank() == true

        return SettingsRespDTO(
            lowBalanceEnabled = enabled,
            lowBalanceThreshold = threshold,
            hasPinSet = hasPin
        )
    }

    @Transactional
    fun updateSettings(dto: UpdateSettingsReqDTO): SettingsRespDTO {
        val me = currentUser.getLoggedUser()
        val accountId = me.accountId ?: throw IllegalArgumentException("No account")

        val account = accountRepository.findByIdForUpdate(accountId)
            ?: throw IllegalArgumentException("Account not found")

        dto.lowBalanceEnabled?.let { account.lowBalanceAlertEnabled = it }
        dto.lowBalanceThreshold?.let { account.lowBalanceThreshold = it }
        accountRepository.save(account)

        dto.newPin?.let { newPin ->
            require(Regex("""^\d{4}$""").matches(newPin)) { "PIN must be exactly 4 digits!" }

            val cardId = account.cardId ?: throw IllegalArgumentException("No card associated with this account!")
            val card = cardRepository.findByIdForUpdate(cardId) ?: throw IllegalArgumentException("Card not found")
            card.pinCode = newPin
            cardRepository.save(card)
        }

        // trimitem mail - am schimbat setarile contului
        TransactionSynchronizationManager.registerSynchronization(object :
            TransactionSynchronization {
            override fun afterCommit() {
                emailService.sendSettingsChangedEmail(
                    to = me.email,
                    fullName = me.fullName
                )
            }
        })

        return getSettings()
    }
}