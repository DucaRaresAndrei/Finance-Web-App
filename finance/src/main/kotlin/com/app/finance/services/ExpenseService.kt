package com.app.finance.services

import com.app.finance.enums.Category
import com.app.finance.models.Expense
import com.app.finance.models.Income
import com.app.finance.models.requests.ExpenseRequestDTO
import com.app.finance.repositories.AccountRepository
import com.app.finance.repositories.ExpenseRepository
import com.app.finance.repositories.IncomeRepository
import com.app.finance.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

@Service
class ExpenseService(
    private val expenseRepository: ExpenseRepository,
    private val incomeRepository: IncomeRepository,
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository,
    private val friendService: FriendService,
    private val currentUser: CurrentUserService,
    private val cardService: CardService,
    private val emailService: EmailNotificationService
) {
    @Transactional
    fun createExpense(expenseRequest: ExpenseRequestDTO): Pair<Expense, Income> {

        if (expenseRequest.amount <= BigDecimal.ZERO) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive")
        }

        // id-ul userului logat
        val myUser = currentUser.getLoggedUser()
        val senderId = myUser.accountId
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No logged account")

        // luam sender si receiver cu lock
        val sender = accountRepository.findByIdForUpdate(senderId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found")

        val receiver = accountRepository.findByIbanForUpdate(expenseRequest.ibanReceiver)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Account with IBAN ${expenseRequest.ibanReceiver} not found!"
            )

        // pin check pentru firme
        cardService.verifyPaymentToFirm(expenseRequest.amount, expenseRequest.ibanReceiver, expenseRequest.pin)

        // verificam balanta pe sender-ul lock-uit
        if (sender.balance < expenseRequest.amount) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds")
        }

        friendService.addFriendAfterTransaction(expenseRequest.ibanReceiver)

        sender.balance = sender.balance.subtract(expenseRequest.amount)
        receiver.balance = receiver.balance.add(expenseRequest.amount)

        accountRepository.save(sender)
        accountRepository.save(receiver)

        val savedExpense = expenseRepository.save(
            Expense(
                description = expenseRequest.description,
                amount = expenseRequest.amount,
                ibanReceiver = receiver.iban,
                ibanSender = sender.iban,
                category = receiver.category
            )
        )

        val savedIncome = incomeRepository.save(
            Income(
                description = expenseRequest.description,
                amount = expenseRequest.amount,
                ibanReceiver = receiver.iban,
                ibanSender = sender.iban
            )
        )

        // email doar dupa commit
        val userReceiver = userRepository.findByAccountId(receiver.id)
        val finalSenderBalance = sender.balance
        val receiverCategory = receiver.category
        val receiverIban = receiver.iban
        val receiverName = userReceiver.fullName

        TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
            override fun afterCommit() {
                if (receiverCategory != Category.USER) {
                    emailService.sendFirmPaymentSuccessEmail(
                        to = myUser.email,
                        myFullName = myUser.fullName,
                        amount = expenseRequest.amount,
                        firmIban = receiverIban,
                        firmName = receiverName,
                        currentBalance = finalSenderBalance
                    )
                } else {
                    emailService.sendTransferSuccessEmail(
                        to = myUser.email,
                        myFullName = myUser.fullName,
                        amount = expenseRequest.amount,
                        destinationIban = receiverIban,
                        destinationName = receiverName,
                        currentBalance = finalSenderBalance
                    )
                }
            }
        })

        return Pair(savedExpense, savedIncome)
    }
}
