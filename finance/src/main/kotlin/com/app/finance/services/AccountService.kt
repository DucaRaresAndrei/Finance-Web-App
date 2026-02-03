package com.app.finance.services

import com.app.finance.mappers.AccountMapper
import com.app.finance.mappers.TransactionMapper
import com.app.finance.models.Account
import com.app.finance.models.requests.AccountRequestDTO
import com.app.finance.models.responses.HistorySummaryDTO
import com.app.finance.repositories.AccountRepository
import com.app.finance.repositories.ExpenseRepository
import com.app.finance.repositories.IncomeRepository
import com.app.finance.repositories.UserRepository
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.springframework.stereotype.Service
import java.math.BigDecimal

@NoArgsConstructor
@AllArgsConstructor
@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
    private val userRepository: UserRepository
) {
    fun createAccount(accountRequest: AccountRequestDTO): Account {
        val iban = generateIban()
        val entity = AccountMapper.toEntity(accountRequest, iban)
        val createdAccount = accountRepository.save(entity)
        return createdAccount
    }

    fun getHistorySummary(iban: String): HistorySummaryDTO? {
        val totalIncome = incomeRepository.countByIbanReceiver(iban)
        val totalExpense = expenseRepository.countByIbanSender(iban)
        val lastIncome = incomeRepository.findTop5ByIbanReceiverOrderByDateDesc(iban).map {
            TransactionMapper.fromIncome(it, senderName = userRepository.findByAccountId((accountRepository.findByIban(it.ibanSender))?.id).fullName)}
        val lastExpense = expenseRepository.findTop5ByIbanSenderOrderByDateDesc(iban).map { TransactionMapper.fromExpense(it,
            receiverName = userRepository.findByAccountId((accountRepository.findByIban(it.ibanReceiver))?.id).fullName) }

        val lastTransactions = (lastIncome + lastExpense).sortedByDescending { it.date }.take(5)
        return HistorySummaryDTO(BigDecimal.valueOf(totalIncome), BigDecimal.valueOf(totalExpense), lastTransactions)
    }

    fun generateIban(): String {
        var iban: String
        do {
            iban = "RO" + (1000000000000000..9999999999999999).random().toString()
        } while (accountRepository.existsByIban(iban))
        return iban
    }
}
