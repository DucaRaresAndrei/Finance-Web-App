package com.app.finance.controllers

import com.app.finance.models.requests.FirmListRequestDTO
import com.app.finance.models.requests.UpgradeFirmRequestDTO
import com.app.finance.models.responses.FirmDTO
import com.app.finance.services.FirmService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/firms")
class FirmController(
    private val firmService: FirmService
) {
    @PostMapping("/upgrade")
    fun upgradeToFirm(@RequestBody req: UpgradeFirmRequestDTO
    ): ResponseEntity<Unit> {
        firmService.upgradeToFirm(req.category)
        return ResponseEntity.ok(Unit)
    }

    @PostMapping
    fun listFirms(@RequestBody req: FirmListRequestDTO
    ): ResponseEntity<List<FirmDTO>> {
        val firms = firmService.listFirms(req.category)
        return ResponseEntity.ok(firms)
    }
}