package com.app.finance.models.responses.statistics

data class DashboardStatsDTO(
    val monthSummary: SummaryDTO,
    val weekSummary: SummaryDTO,
    val weeklySpending: List<WeeklyBarDTO>,
    val spendingByCategory: List<CategorySpendingDTO>
)
