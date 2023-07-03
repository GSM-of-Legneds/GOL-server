package dev.yangsijun.gol.common.riot.dto

import java.time.LocalDateTime

data class MatchIdsApiRequest(
    val puuid: String,
    val start: Int,
    val count: Int,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)
