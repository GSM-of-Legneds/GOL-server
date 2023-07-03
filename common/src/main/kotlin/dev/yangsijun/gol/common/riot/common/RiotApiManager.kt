package dev.yangsijun.gol.common.riot.common

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
data class RiotApiManager(
    val riotProperties: RiotProperties
) {

    val apiKey: String = riotProperties.API_KEY

    fun getHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.add("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8")
        headers.add(RiotApiUtil.HEADER_NAME, riotProperties.API_KEY)
        return headers
    }
}
