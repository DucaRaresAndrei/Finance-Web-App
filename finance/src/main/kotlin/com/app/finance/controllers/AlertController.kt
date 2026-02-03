package com.app.finance.controllers

import com.app.finance.models.responses.LowBalanceAlertDTO
import com.app.finance.services.AlertService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/alerts")
class AlertController(
    private val alertService: AlertService
) {
    @GetMapping("/low-balance")
    fun checkLowBalance(): ResponseEntity<LowBalanceAlertDTO> {
        return ResponseEntity.ok(alertService.checkLowBalance())
    }
}