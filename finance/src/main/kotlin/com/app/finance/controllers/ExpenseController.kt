package com.app.finance.controllers

import com.app.finance.models.Expense
import com.app.finance.models.Income
import com.app.finance.models.requests.ExpenseRequestDTO
import com.app.finance.services.ExpenseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/expense")
class ExpenseController (
    private val expenseService: ExpenseService
) {
    // logica de transfer bani de la un user la altul
    @PostMapping
    fun createExpense(@RequestBody expenseRequest: ExpenseRequestDTO): ResponseEntity<Pair<Expense, Income>> {
        return ResponseEntity.ok(expenseService.createExpense(expenseRequest))
    }
}
