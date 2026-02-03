package com.app.finance.services

import com.app.finance.enums.Category
import com.app.finance.mappers.TransactionMapper
import com.app.finance.models.responses.HistorySummaryDTO
import com.app.finance.models.responses.TransactionDTO
import com.app.finance.repositories.AccountRepository
import com.app.finance.repositories.ExpenseRepository
import com.app.finance.repositories.IncomeRepository
import com.app.finance.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class HistoryService(
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository,
    private val currentUserService: CurrentUserService
) {
    fun fullTransactionHistory(): HistorySummaryDTO? {
        val account = currentUserService.getLoggedAccount()
        val iban = account.iban
        val totalIncome = incomeRepository.countByIbanReceiver(iban)
        val totalExpense = expenseRepository.countByIbanSender(iban)

        val lastIncome = incomeRepository.findByIbanReceiverOrderByDateDesc(iban).map {
            TransactionMapper.fromIncome(it,
                senderName = userRepository.findByAccountId((accountRepository.findByIban(it.ibanSender))?.id).fullName)
        }
        val lastExpense = expenseRepository.findByIbanSenderOrderByDateDesc(iban).map {
            TransactionMapper.fromExpense(it,
                receiverName = userRepository.findByAccountId((accountRepository.findByIban(it.ibanReceiver))?.id).fullName)
        }
        val lastTransactions = (lastIncome + lastExpense).sortedByDescending { it.date }
        return HistorySummaryDTO(totalIncome.toBigDecimal(), totalExpense.toBigDecimal(), lastTransactions)
    }

    fun last4UserPeerTransactions(): List<TransactionDTO> {
        val me = currentUserService.getLoggedAccount()
        val myIban = me.iban

        val incomesFromUsers = incomeRepository.findByIbanReceiverOrderByDateDesc(myIban)
            .mapNotNull { inc ->
                val senderAcc = accountRepository.findByIban(inc.ibanSender) ?: return@mapNotNull null
                // doar daca cel care trimite e USER
                if (senderAcc.category != Category.USER) return@mapNotNull null
                if (inc.ibanReceiver == inc.ibanSender) return@mapNotNull null
                val senderName = userRepository.findByAccountId(senderAcc.id).fullName
                TransactionMapper.fromIncome(inc, senderName = senderName)
            }

        val expensesToUsers = expenseRepository.findByIbanSenderOrderByDateDesc(myIban)
            .mapNotNull { exp ->
                val receiverAcc = accountRepository.findByIban(exp.ibanReceiver) ?: return@mapNotNull null
                // doar daca cel care primeste e USER
                if (receiverAcc.category != Category.USER) return@mapNotNull null
                val receiverName = userRepository.findByAccountId(receiverAcc.id).fullName
                TransactionMapper.fromExpense(exp, receiverName = receiverName)
            }

        return (incomesFromUsers + expensesToUsers).sortedByDescending { it.date }.take(4)
    }

    fun last4FirmPayments(): List<TransactionDTO> {
        val me = currentUserService.getLoggedAccount()
        val myIban = me.iban

        val expensesToFirms = expenseRepository.findByIbanSenderOrderByDateDesc(myIban)
            .mapNotNull { exp ->
                val receiverAcc = accountRepository.findByIban(exp.ibanReceiver) ?: return@mapNotNull null
                if (receiverAcc.category == Category.USER) return@mapNotNull null
                val receiverName = userRepository.findByAccountId(receiverAcc.id).fullName
                TransactionMapper.fromExpense(exp, receiverName = receiverName)
            }

        return expensesToFirms.sortedByDescending { it.date }.take(4)
    }
}