package com.app.notification.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class MailConfig(
    @Value("\${spring.mail.host}") private val host: String,
    @Value("\${spring.mail.port}") private val port: Int,

    @Value("\${MAIL_USERNAME:}") private val mailUsername: String,
    @Value("\${MAIL_PASSWORD:}") private val mailPassword: String,

    @Value("\${MAIL_USERNAME_FILE:}") private val mailUsernameFile: String,
    @Value("\${MAIL_PASSWORD_FILE:}") private val mailPasswordFile: String
) {
    @Bean
    fun javaMailSender(): JavaMailSender {
        val username = SecretReader.read(mailUsername, mailUsernameFile) ?: ""
        val password = SecretReader.read(mailPassword, mailPasswordFile) ?: ""

        println("MAIL CONFIG => host=$host port=$port user=${username.take(3)}*** passLen=${password.length}")

        val impl = JavaMailSenderImpl()
        impl.host = host
        impl.port = port
        impl.username = username
        impl.password = password

        val props = Properties()
        props["mail.transport.protocol"] = "smtp"

        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.starttls.required"] = "true"

        props["mail.smtp.connectiontimeout"] = "5000"
        props["mail.smtp.timeout"] = "5000"
        props["mail.smtp.writetimeout"] = "5000"

        props["mail.smtp.ssl.trust"] = "*"
        props["mail.smtp.ssl.checkserveridentity"] = "false"
        props["mail.smtp.ssl.protocols"] = "TLSv1.2 TLSv1.3"

        impl.javaMailProperties = props

        return impl
    }
}
