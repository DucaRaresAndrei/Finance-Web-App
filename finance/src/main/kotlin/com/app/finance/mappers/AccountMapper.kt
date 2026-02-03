package com.app.finance.mappers

import com.app.finance.enums.Category
import com.app.finance.models.Account
import com.app.finance.models.Card
import com.app.finance.models.requests.AccountRequestDTO
import com.app.finance.models.responses.AccountResponseDTO
import com.app.finance.models.responses.HistorySummaryDTO
import java.math.BigDecimal

object AccountMapper {
    fun toEntity(dto: AccountRequestDTO, iban: String): Account =
        Account(
            id = null,
            cardId = null,
            //history = null,
            category = dto.category ?: Category.USER,
            iban = iban,
            balance = dto.initialBalance ?: BigDecimal.ZERO,
            friends = emptyList())

    fun toResponseDTO(account: Account?, card: Card?, history: HistorySummaryDTO? = null): AccountResponseDTO =
        AccountResponseDTO(
            card = card?.let { CardMapper.toResponseDTO(it) },
            history = history,
            category = account!!.category,
            iban = account.iban,
            balance = account.balance
        )
}