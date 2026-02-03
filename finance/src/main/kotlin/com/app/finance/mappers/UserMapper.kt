package com.app.finance.mappers

import com.app.finance.models.Account
import com.app.finance.models.Card
import com.app.finance.models.User
import com.app.finance.models.responses.HistorySummaryDTO
import com.app.finance.models.responses.UserResponseDTO

object UserMapper {

    fun toResponseDTO(
        user: User,
        account: Account? = null,
        card: Card?,
        history: HistorySummaryDTO? = null
    ): UserResponseDTO =
        UserResponseDTO(
            fullName = user.fullName,
            email = user.email,
            account = account?.let { AccountMapper.toResponseDTO(it, card, history) }
        )
}
