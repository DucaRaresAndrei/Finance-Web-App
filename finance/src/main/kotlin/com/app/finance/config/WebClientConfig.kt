package com.app.finance.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    // cream un WebClient ca Bean pentru a il putea injecta in servicii
    // WebClient e folosit pentru apeluri HTTP catre alte microservicii
    @Bean
    fun webClient(): WebClient = WebClient.builder().build()
}