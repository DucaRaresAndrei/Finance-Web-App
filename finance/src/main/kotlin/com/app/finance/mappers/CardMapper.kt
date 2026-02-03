package com.app.finance.mappers

import com.app.finance.models.Card
import com.app.finance.models.responses.CardResponseDTO
import java.time.YearMonth

object CardMapper {
    private fun mask(number: String) =
        "**** **** **** ${number.takeLast(4)}"

    fun toResponseDTO(card: Card) =
        CardResponseDTO(
            id = requireNotNull(card.id),
            cardNumberMasked = mask(card.cardNumber),
            cardNumber = card.cardNumber,
            expirationDate = YearMonth.from(card.expirationDate),
            cvvNumber = card.cvvNumber
        )
}