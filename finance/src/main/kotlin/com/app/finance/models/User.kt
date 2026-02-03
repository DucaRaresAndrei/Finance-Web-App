package com.app.finance.models

import com.app.finance.enums.Role
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "keycloak_id", nullable = false, unique = true)
    val keycloakId: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(name = "full_name", nullable = false)
    val fullName: String,

    @Column(name = "account_id")
    val accountId: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.USER
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_${role.name}"))

    override fun getPassword(): String = ""

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}
