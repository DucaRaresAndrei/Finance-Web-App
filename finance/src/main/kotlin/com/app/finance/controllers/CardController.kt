package com.app.finance.controllers

import com.app.finance.models.requests.TopupRequestDTO
import com.app.finance.services.CardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/card")
class CardController (
    private val cardService: CardService
){

    @DeleteMapping
    fun deleteCard(): ResponseEntity<Unit?> {
        return ResponseEntity.ok(cardService.deleteCard())
    }

    @PostMapping("/topup")
    fun topup(
        @RequestBody req: TopupRequestDTO
    ): ResponseEntity<Unit> {
        cardService.addMoney(req.amount, req.description)
        return ResponseEntity.ok(Unit)
    }

//    @PostMapping("/payment/check")
//    fun checkTransfer(@RequestBody req: VerifyPinRequestDTO): ResponseEntity<TransferCheckResponseDTO> {
//        val requires = cardService.needsPin(req.amount, req.recipientEmail)
//
//        val msg = if (requires) "PIN is required for transfers over 100 RON to firm accounts." else null
//
//        return ResponseEntity.ok(TransferCheckResponseDTO(requires, msg))
//    }
}