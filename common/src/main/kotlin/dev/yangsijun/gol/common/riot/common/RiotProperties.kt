package dev.yangsijun.gol.common.riot.common

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class RiotProperties(
    @Value("\${riot.api-key}") val API_KEY: String,
) {
}
