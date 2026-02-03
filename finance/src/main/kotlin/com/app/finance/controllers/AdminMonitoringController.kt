package com.app.finance.controllers

import com.app.finance.enums.Category
import com.app.finance.services.PrometheusQueryService
import com.app.finance.services.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

// Controller pentru monitoring admin
@RestController
@RequestMapping("/admin/monitoring")
class AdminMonitoringController(
    private val prom: PrometheusQueryService,
    private val userService: UserService
) {
    private val window = "3m"

    private val selector = "job=\"finance-api\",uri!~\"/actuator.*\",uri!~\"/admin.*\""

    // Averages pe ultimele 3 minute
    @GetMapping("/summary")
    fun summary(): MonitoringSummaryDTO {
        val rps = prom.querySingle("sum(rate(http_server_requests_seconds_count{$selector}[$window]))")

        val err4 = prom.querySingle(
            "sum(rate(http_server_requests_seconds_count{$selector,status=~\"4..\"}[$window]))"
        )

        val err5 = prom.querySingle(
            "sum(rate(http_server_requests_seconds_count{$selector,status=~\"5..\"}[$window]))"
        )

        val p95Sec = prom.querySingle(
            "histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket{$selector}[$window])) by (le))"
        )

        val up = prom.querySingle("max_over_time(up{job=\"finance-api\"}[2m])") >= 1.0

        // afisaj per minut in medie pe ultimele 3 minute
        return MonitoringSummaryDTO(
            requestsPerSecond = rps * 60,
            errors4xxPerSecond = err4 * 60,
            errors5xxPerSecond = err5 * 60,
            p95LatencyMs = p95Sec * 1000.0,
            financeApiUp = up
        )
    }

    // Rate per status (2xx/4xx/5xx)
    @GetMapping("/requests-by-status")
    fun requestsByStatus(): List<LabelValueDTO> {
        val data = prom.queryVector(
            "sum(rate(http_server_requests_seconds_count{$selector}[$window])) by (status)"
        )
        return data.map { (labels, value) ->
            LabelValueDTO(label = labels["status"] ?: "unknown", value = value * 60)
        }.sortedByDescending { it.value }
    }

    // Top endpoints (dupa rate)
    @GetMapping("/top-endpoints")
    fun topEndpoints(@RequestParam(defaultValue = "10") limit: Int): List<EndpointRateDTO> {
        val q = "topk($limit, sum(rate(http_server_requests_seconds_count{$selector}[$window])) by (uri, method))"
        val data = prom.queryVector(q)

        return data.map { (labels, value) ->
            EndpointRateDTO(
                uri = labels["uri"] ?: "unknown",
                method = labels["method"] ?: "unknown",
                rps = value * 60
            )
        }.sortedByDescending { it.rps }
    }

    // Starea serviciilor
    @GetMapping("/services-up")
    fun servicesUp(): List<ServiceUpDTO> {
        val data = prom.queryVector("max_over_time(up[2m])")
        return data.map { (labels, value) ->
            ServiceUpDTO(
                job = labels["job"] ?: "unknown",
                instance = labels["instance"] ?: "unknown",
                up = value >= 1.0
            )
        }.sortedWith(compareBy({ it.job }, { it.instance }))
    }

//    @GetMapping("/debug-up")
//    fun debugUp(): Map<String, Any?> {
//        val q2 = "up{job=\"finance-api\"}"
//
//        val raw2 = prom.queryRaw(q2)
//        val err2 = prom.lastError()
//
//        return mapOf(
//            "baseUrl" to prom.baseUrl(),
//            "query" to q2,
//            "raw" to raw2,
//            "err" to err2
//        )
//    }

    @GetMapping("/users")
    fun users(): List<AdminUserRowDTO> {
        return userService.getAllUsers()
    }
}

data class MonitoringSummaryDTO(
    val requestsPerSecond: Double,
    val errors4xxPerSecond: Double,
    val errors5xxPerSecond: Double,
    val p95LatencyMs: Double,
    val financeApiUp: Boolean
)

data class LabelValueDTO(val label: String, val value: Double)

data class EndpointRateDTO(
    val uri: String,
    val method: String,
    val rps: Double
)

data class ServiceUpDTO(
    val job: String,
    val instance: String,
    val up: Boolean
)

data class AdminUserRowDTO(
    val username: String,
    val email: String,
    val category: Category
)
