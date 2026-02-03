package com.app.finance.services

import com.app.finance.enums.Category
import com.app.finance.models.Account
import com.app.finance.models.Card
import com.app.finance.models.Income
import com.app.finance.repositories.AccountRepository
import com.app.finance.repositories.CardRepository
import com.app.finance.repositories.IncomeRepository
import com.app.finance.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.random.Random

//private val PIN_THRESHOLD: BigDecimal = BigDecimal("100")

@Service
class CardService(
    private val cardRepository: CardRepository,
    private val accountRepository: AccountRepository,
    private val incomeRepository: IncomeRepository,
    private val userRepository: UserRepository,
    private val currentUser: CurrentUserService
) {
    companion object {
        private val PIN_THRESHOLD = BigDecimal("100")
        private val PIN_REGEX = Regex("""^\d{4}$""")
    }

    fun getCardForAccount(account: Account) =
        account.cardId?.let { cardRepository.findById(it).orElse(null) }

    fun generateNumber(n : Int): String {
        val builder = StringBuilder()
        repeat(n) {
            builder.append(Random.nextInt(0, 10))
        }
        return builder.toString()
    }

    fun createCard(): Card {
        val card = Card(
            cardNumber = generateNumber(16),
            cvvNumber = generateNumber(3),
            expirationDate = LocalDateTime.now().plusYears(5)
        )
        val savedCard = cardRepository.save(card)

        return savedCard
    }

    @Transactional
    fun addMoney(amount: BigDecimal, description: String? = null) {
        if (amount <= BigDecimal.ZERO) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive!")
        }

        val me = currentUser.getLoggedUser()
        val accountId = me.accountId ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No logged account")

        val account = accountRepository.findByIdForUpdate(accountId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found")

        account.balance = account.balance.add(amount)
        accountRepository.save(account)

        incomeRepository.save(
            Income(
                amount = amount,
                description = description ?: "Card loaded at ATM",
                date = LocalDateTime.now(),
                ibanSender = account.iban,
                ibanReceiver = account.iban
            )
        )
    }

    fun needsPinForFirm(amount: BigDecimal, firmIban: String): Boolean {
        val firmAccount = accountRepository.findByIban(firmIban)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Account with IBAN $firmIban not found!")

        return firmAccount.category != Category.USER && amount > PIN_THRESHOLD
    }

    @Transactional
    fun verifyPaymentToFirm(amount: BigDecimal, firmIban: String, providedPin: String?): Boolean {
        if (!needsPinForFirm(amount, firmIban)) return true

        if (providedPin == null) {
            throw ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, "PIN is required for payments over $PIN_THRESHOLD to firm accounts!")
        }
        if (!PIN_REGEX.matches(providedPin)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid PIN format!")
        }

        val myAccountId = currentUser.getLoggedUser().accountId ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No logged account")
        val myAccount = accountRepository.findById(myAccountId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found") }

        val cardId = myAccount.cardId ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No card associated!")
        val card = cardRepository.findByIdForUpdate(cardId)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No card associated!")

        val currentPin = card.pinCode?.trim()
        if (currentPin.isNullOrBlank()) {
            // primul payment => pin-ul se va seta automat
            card.pinCode = providedPin
            cardRepository.save(card)
            return true
        }

        if (currentPin != providedPin) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid PIN. Please try again!")
        }

        return true
    }

    fun deleteCard() {
        val account = currentUser.getLoggedAccount()
        if (account.cardId == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No card associated with this account!")
        }
        cardRepository.deleteById(account.cardId)
    }
}