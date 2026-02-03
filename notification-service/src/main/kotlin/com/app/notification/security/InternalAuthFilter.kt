package com.app.notification.security

import com.app.notification.config.SecretReader
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class InternalAuthFilter(
    @Value("\${internal.token}") private val internalTokenEnv: String,
    @Value("\${INTERNAL_TOKEN_FILE:}") private val internalTokenFile: String
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.requestURI.startsWith("/internal/")) {
            // verifica ca tokenul asteptat si cel trimis de la finance-api sa coincida
            val expected = SecretReader.read(internalTokenEnv, internalTokenFile) ?: ""
            val provided = request.getHeader("X-Internal-Token") ?: ""

            if (expected.isBlank() || provided != expected) {
                response.status = 401
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}
