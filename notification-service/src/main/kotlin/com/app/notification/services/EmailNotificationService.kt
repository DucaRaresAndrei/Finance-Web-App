package com.app.notification.services

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class EmailNotificationService(
    private val mailSender: JavaMailSender,
    @Value("\${mail.enabled:true}") private val mailEnabled: Boolean,
    @Value("\${mail.from:no-reply@finance.local}") private val mailFrom: String
) {
    private val log = LoggerFactory.getLogger(EmailNotificationService::class.java)

    fun sendAccountCreatedEmail(
        to: String,
        fullName: String
    ) {
        if (!mailEnabled) return

        val subject = "Account created successfully"
        val html = """
            <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #222;">
              <h2 style="margin: 0 0 12px 0;">Welcome, ${escape(fullName)}!</h2>
        
              <p style="margin: 0 0 12px 0;">
                Your account has been successfully created on 
                <span style="font-weight: 700; color: #1a73e8;">FinanceWebApp</span>.
              </p>
        
              <p style="margin: 0 0 12px 0;">
                If you did not initiate this request, you may safely disregard this email.
              </p>
        
              <hr style="border: 0; border-top: 1px solid #e6e6e6; margin: 16px 0;" />
        
              <p style="font-size: 12px; color: #666; margin: 0;">
                This is an automated message. Please do not reply.
              </p>
            </div>
        """.trimIndent()

        sendHtmlEmail(to, subject, html)
    }

    @Async
    fun sendTransferSuccessEmail(
        to: String,
        myFullName: String,
        amount: BigDecimal,
        destinationIban: String,
        destinationName: String?,
        currentBalance: BigDecimal
    ) {
        if (!mailEnabled) return

        val subject = "Transfer completed successfully"
        val html = """
            <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #222;">
              <h2 style="margin: 0 0 12px 0;">Transfer confirmed on FinanceWebApp</h2>

              <p style="margin: 0 0 12px 0;">
                Hello ${escape(myFullName)}, your transfer was completed successfully.
              </p>

              <div style="background:#f7f7f7; padding:12px 14px; border-radius:10px; margin: 12px 0;">
                <p style="margin: 0;">
                  <b>Amount:</b> ${amount} RON<br/>
                  <b>To (IBAN):</b> ${escape(destinationIban)}<br/>
                  <b>Recipient:</b> ${escape(destinationName ?: "User")}<br/>
                  <b>Current balance:</b> ${currentBalance} RON<br/>
                </p>
              </div>

              <p style="margin: 0 0 12px 0;">
                If you did not authorize this transaction, please secure your account immediately!
              </p>

              <hr style="border: 0; border-top: 1px solid #e6e6e6; margin: 16px 0;" />
        
              <p style="font-size: 12px; color: #666; margin: 0;">
                This is an automated message. Please do not reply.
              </p>
            </div>
        """.trimIndent()

        sendHtmlEmail(to, subject, html)
    }

    @Async
    fun sendFirmPaymentSuccessEmail(
        to: String,
        myFullName: String,
        amount: BigDecimal,
        firmIban: String,
        firmName: String?,
        currentBalance: BigDecimal
    ) {
        if (!mailEnabled) return

        val subject = "Payment completed successfully"
        val html = """
            <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #222;">
              <h2 style="margin: 0 0 12px 0;">Payment confirmed on FinanceWebApp</h2>

              <p style="margin: 0 0 12px 0;">
                Hello ${escape(myFullName)}, your payment was processed successfully.
              </p>

              <div style="background:#f7f7f7; padding:12px 14px; border-radius:10px; margin: 12px 0;">
                <p style="margin: 0;">
                  <b>Amount:</b> ${amount} RON<br/>
                  <b>To (IBAN):</b> ${escape(firmIban)}<br/>
                  <b>Merchant:</b> ${escape(firmName ?: "Merchant")}<br/>
                  <b>Current balance:</b> ${currentBalance} RON<br/>
                </p>
              </div>

              <p style="margin: 0 0 12px 0;">
                If you did not authorize this payment, please secure your account immediately!
              </p>

              <hr style="border: 0; border-top: 1px solid #e6e6e6; margin: 16px 0;" />
        
              <p style="font-size: 12px; color: #666; margin: 0;">
                This is an automated message. Please do not reply.
              </p>
            </div>
        """.trimIndent()

        sendHtmlEmail(to, subject, html)
    }

    @Async
    fun sendFriendAddedEmail(
        to: String,
        ownerFullName: String,
        friendFullName: String?,
        friendIban: String
    ) {
        if (!mailEnabled) return

        val subject = "Friend added successfully"
        val html = """
            <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #222;">
              <h2 style="margin: 0 0 12px 0;">Friend added on FinanceWebApp</h2>

              <p style="margin: 0 0 12px 0;">
                Hello ${escape(ownerFullName)}, you successfully added a new friend.
              </p>

              <div style="background:#f7f7f7; padding:12px 14px; border-radius:10px; margin: 12px 0;">
                <p style="margin: 0;">
                  <b>Friend username:</b> ${escape(friendFullName ?: "N/A")}<br/>
                  <b>Friend IBAN:</b> ${escape(friendIban)}
                </p>
              </div>

              <hr style="border: 0; border-top: 1px solid #e6e6e6; margin: 16px 0;" />
        
              <p style="font-size: 12px; color: #666; margin: 0;">
                This is an automated message. Please do not reply.
              </p>
            </div>
        """.trimIndent()

        sendHtmlEmail(to, subject, html)
    }

    @Async
    fun sendSettingsChangedEmail(
        to: String,
        fullName: String
    ) {
        if (!mailEnabled) return

        val subject = "Settings updated"
        val html = """
            <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #222;">
              <h2 style="margin: 0 0 12px 0;">Settings updated on FinanceWebApp</h2>

              <p style="margin: 0 0 12px 0;">
                Hello ${escape(fullName)}, your account settings have been updated successfully.
              </p>

              <p style="margin: 0 0 12px 0;">
                If you did not make this change, please secure your account immediately!
              </p>

              <hr style="border: 0; border-top: 1px solid #e6e6e6; margin: 16px 0;" />
        
              <p style="font-size: 12px; color: #666; margin: 0;">
                This is an automated message. Please do not reply.
              </p>
            </div>
        """.trimIndent()

        sendHtmlEmail(to, subject, html)
    }

    private fun sendHtmlEmail(to: String, subject: String, html: String) {
        if (!mailEnabled) {
            log.info("Mail disabled. Skipping email subject='{}' to={}", subject, to)
            return
        }

        try {
            val mimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, "UTF-8")
            helper.setFrom(mailFrom)
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(html, true)

            mailSender.send(mimeMessage)
            log.info("Email sent subject='{}' to={}", subject, to)
        } catch (ex: Exception) {
            // nu pica register doar pentru ca a picat mailul
            log.error("Failed to send email subject='{}' to={}. Reason={}", subject, to, ex.message, ex)
        }
    }

    private fun escape(s: String): String =
        s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
}
