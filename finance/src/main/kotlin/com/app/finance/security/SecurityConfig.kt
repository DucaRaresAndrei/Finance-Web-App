package com.app.finance.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {

    // Actuator chain (prometheus + health fara auth)
    @Bean
    @Order(0)
    fun actuatorSecurity(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/actuator/**")
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/actuator/health", "/actuator/prometheus").permitAll()
                    .anyRequest().denyAll()
            }
            .csrf { it.disable() }

        return http.build()
    }

    // Main app chain
    @Bean
    @Order(1)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors(withDefaults())
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                    // public
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        // "/actuator/health",
                        "/auth",
                        "/auth/**"
                    ).permitAll()

                    // doar pentru admin (va urma sa mai lucrez)
                    .requestMatchers("/admin/**").hasRole("ADMIN")

                    // user autentificat
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(keycloakJwtAuthenticationConverter())
                }
            }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val cfg = CorsConfiguration()
        cfg.allowedOrigins = listOf(
            "http://localhost:8084",
            "http://127.0.0.1:8084"
        )
        cfg.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        cfg.allowedHeaders = listOf("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin")
        cfg.exposedHeaders = listOf("Authorization")
        cfg.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", cfg)
        return source
    }

    // extrage rolurile din Keycloak si le mapeaza pentru Spring Security
    @Bean
    fun keycloakJwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt: Jwt ->
            val realmAccess = jwt.claims["realm_access"] as? Map<*, *> ?: emptyMap<Any, Any>()
            val roles = realmAccess["roles"] as? Collection<*> ?: emptyList<Any>()

            roles
                .filterIsInstance<String>()
                .map { role -> SimpleGrantedAuthority("ROLE_$role") }
        }
        return converter
    }
}
