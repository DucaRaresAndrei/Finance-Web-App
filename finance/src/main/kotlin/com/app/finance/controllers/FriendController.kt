package com.app.finance.controllers

import com.app.finance.models.Account
import com.app.finance.models.requests.AddFriendRequestDTO
import com.app.finance.models.requests.SearchFriendRequestDTO
import com.app.finance.models.responses.FriendResponseDTO
import com.app.finance.services.FriendService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/friends")
class FriendController(
    private val friendService: FriendService
) {
    @PostMapping("/add")
    fun addFriend(@RequestBody req: AddFriendRequestDTO): ResponseEntity<FriendResponseDTO> {
        val dto = friendService.addFriendByIban(req.iban)
        return ResponseEntity.ok(dto)
    }

    @GetMapping
    fun getFriends(): ResponseEntity<List<FriendResponseDTO>> {
        return ResponseEntity.ok(friendService.listFriends())
    }

    @PostMapping("/search")
    fun searchFriends(@RequestBody req: SearchFriendRequestDTO): ResponseEntity<List<FriendResponseDTO>> {
        return ResponseEntity.ok(friendService.searchFriends(req.nameOrMail))
    }
}