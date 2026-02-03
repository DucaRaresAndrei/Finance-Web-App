package com.app.finance.services

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.io.File
import java.math.BigDecimal

@Service
class EmailNotificationService(
    private val webClient: WebClient,
    // base URL catre notification-service
    @Value("\${notifications.base-url}") private val baseUrl: String,
    @Value("\${notifications.internal-token}") private val internalTokenEnv: String,
    @Value("\${notifications.internal-token-file:}") private val internalTokenFile: String
) {
    private val log = LoggerFactory.getLogger(EmailNotificationService::class.java)

    // returneaza tokenul intern, fie din proprietati, fie din fisierul secret
    private fun internalToken(): String {
        if (internalTokenEnv.isNotBlank()) return internalTokenEnv.trim()

        if (internalTokenFile.isNotBlank()) {
            val f = File(internalTokenFile)
            if (f.exists()) return f.readText().trim()
        }
        return ""
    }

    fun sendAccountCreatedEmail(
        to: String,
        fullName: String
    ) = post("/internal/email/account-created", mapOf("to" to to, "fullName" to fullName))

    fun sendTransferSuccessEmail(
        to: String,
        myFullName: String,
        amount: BigDecimal,
        destinationIban: String,
        destinationName: String?,
        currentBalance: BigDecimal
    ) = post("/internal/email/transfer-success", mapOf(
        "to" to to,
        "myFullName" to myFullName,
        "amount" to amount,
        "destinationIban" to destinationIban,
        "destinationName" to destinationName,
        "currentBalance" to currentBalance
    ))

    fun sendFirmPaymentSuccessEmail(
        to: String,
        myFullName: String,
        amount: BigDecimal,
        firmIban: String,
        firmName: String?,
        currentBalance: BigDecimal
    ) = post("/internal/email/firm-payment-success", mapOf(
        "to" to to,
        "myFullName" to myFullName,
        "amount" to amount,
        "firmIban" to firmIban,
        "firmName" to firmName,
        "currentBalance" to currentBalance
    ))

    fun sendFriendAddedEmail(
        to: String,
        ownerFullName: String,
        friendFullName: String?,
        friendIban: String
    ) = post("/internal/email/friend-added", mapOf(
        "to" to to,
        "ownerFullName" to ownerFullName,
        "friendFullName" to friendFullName,
        "friendIban" to friendIban
    ))

    fun sendSettingsChangedEmail(
        to: String,
        fullName: String
    ) = post("/internal/email/settings-changed", mapOf("to" to to, "fullName" to fullName))

    private fun post(path: String, body: Any) {
        try {
            // citim tokenul intern
            val token = internalToken()
            if (token.isBlank()) {
                log.warn("No internal token configured; skipping notification call to {}", path)
                return
            }

            // serviciul de mail lucreaza in paralel, spre a nu bloca flow-ul
            webClient.post()
                .uri(baseUrl + path)
                .header("X-Internal-Token", token)
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity() // asteptam statusul requestului
                .doOnError { ex ->
                    log.error("Notification call failed path={} reason={}", path, ex.message)
                }
                .subscribe() // pornim apelul asincron

        } catch (ex: Exception) {
            log.error("Notification call failed path={} reason={}", path, ex.message, ex)
        }
    }
}
