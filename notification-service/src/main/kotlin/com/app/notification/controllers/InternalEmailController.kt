package com.app.notification.controllers

import com.app.notification.dto.*
import com.app.notification.services.EmailNotificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/internal/email")
class InternalEmailController(
    private val emailService: EmailNotificationService
) {
    @PostMapping("/account-created")
    fun accountCreated(@RequestBody req: AccountCreatedReq): ResponseEntity<Void> {
        emailService.sendAccountCreatedEmail(req.to, req.fullName)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/transfer-success")
    fun transferSuccess(@RequestBody req: TransferSuccessReq): ResponseEntity<Void> {
        emailService.sendTransferSuccessEmail(
            req.to, req.myFullName, req.amount, req.destinationIban, req.destinationName, req.currentBalance
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/firm-payment-success")
    fun firmPaymentSuccess(@RequestBody req: FirmPaymentSuccessReq): ResponseEntity<Void> {
        emailService.sendFirmPaymentSuccessEmail(
            req.to, req.myFullName, req.amount, req.firmIban, req.firmName, req.currentBalance
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/friend-added")
    fun friendAdded(@RequestBody req: FriendAddedReq): ResponseEntity<Void> {
        emailService.sendFriendAddedEmail(req.to, req.ownerFullName, req.friendFullName, req.friendIban)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/settings-changed")
    fun settingsChanged(@RequestBody req: SettingsChangedReq): ResponseEntity<Void> {
        emailService.sendSettingsChangedEmail(req.to, req.fullName)
        return ResponseEntity.ok().build()
    }
}
