package com.app.finance.controllers

import com.app.finance.models.requests.UpdateSettingsReqDTO
import com.app.finance.models.responses.SettingsRespDTO
import com.app.finance.services.SettingsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/settings")
class SettingsController(
    private val settingsService: SettingsService,
) {
    @GetMapping
    fun getSettings(): ResponseEntity<SettingsRespDTO> {
        return ResponseEntity.ok(settingsService.getSettings())
    }

    @PutMapping
    fun updateSettings(@RequestBody dto: UpdateSettingsReqDTO): ResponseEntity<SettingsRespDTO> {
        return ResponseEntity.ok(settingsService.updateSettings(dto))
    }
}