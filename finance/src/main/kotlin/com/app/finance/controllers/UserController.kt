package com.app.finance.controllers

import com.app.finance.models.responses.UserResponseDTO
import com.app.finance.services.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
//@RequestMapping("/user")
class UserController(private val userService: UserService) {

    @GetMapping("/user")
    fun getCurrentUser(@AuthenticationPrincipal jwt: Jwt): UserResponseDTO {
        return userService.ensureUserFromKeycloak(jwt)
    }

//    @GetMapping
//    fun getMyUserAccount(): ResponseEntity<UserResponseDTO?> {
//        return ResponseEntity.ok(userService.getUser())
//    }

//    @DeleteMapping()
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    fun deleteUser() {
//        userService.deleteUser()
//    }
}