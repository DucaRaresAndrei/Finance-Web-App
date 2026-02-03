package com.app.finance.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriUtils
import java.net.URI
import java.nio.charset.StandardCharsets
import java.time.Duration

// serviciu folosit pentru a avea un "admin summary" in backend, fara Grafana
@Service
class PrometheusQueryService(
    private val webClient: WebClient,
    @Value("\${monitoring.prometheus-base-url}") private val prometheusBaseUrl: String
) {
    private val mapper = jacksonObjectMapper()

    // pentru debug: ultimul mesaj de eroare
    @Volatile private var lastError: String? = null
    fun baseUrl(): String = prometheusBaseUrl
    fun lastError(): String? = lastError

    fun queryRaw(query: String): String? = doQueryRaw(query)

    // parseaza raspunsul Prometheus (labels + value)
    fun queryVector(query: String): List<Pair<Map<String, String>, Double>> {
        val json = doQueryRaw(query) ?: return emptyList()

        val root = mapper.readTree(json)
        if (root.path("status").asText() != "success") {
            println("Prometheus returned error: $json")
            return emptyList()
        }

        val resultArr = root.at("/data/result")
        if (!resultArr.isArray) return emptyList()

        val out = mutableListOf<Pair<Map<String, String>, Double>>()

        for (item in resultArr) {
            val metricNode = item.get("metric")
            val valueNode = item.at("/value/1")

            val labels = mutableMapOf<String, String>()
            if (metricNode != null && metricNode.isObject) {
                metricNode.fields().forEachRemaining { (k, v) ->
                    labels[k] = v.asText("")
                }
            }

            val value = valueNode?.asText()?.toDoubleOrNull() ?: 0.0
            out.add(labels to value)
        }

        return out
    }

    // returneaza primul rezultat numeric (sau 0 daca nu exista)
    fun querySingle(query: String): Double {
        return queryVector(query).firstOrNull()?.second ?: 0.0
    }

    private fun doQueryRaw(query: String): String? {
        val q = query.trim()
        val encoded = UriUtils.encodeQueryParam(q, StandardCharsets.UTF_8)

        val base = prometheusBaseUrl.trimEnd('/')
        val uri = URI.create("$base/api/v1/query?query=$encoded")

        return try {
            lastError = null
            webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String::class.java)
                .block(Duration.ofSeconds(3))
        } catch (e: Exception) {
            lastError = "${e::class.qualifiedName}: ${e.message}"
            null
        }
    }
}
