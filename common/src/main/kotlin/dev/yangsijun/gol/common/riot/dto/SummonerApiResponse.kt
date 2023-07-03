package dev.yangsijun.gol.common.riot.dto

data class SummonerApiResponse(
    val accountId: String,
    val profileIconId: Int,
    val revisionDate: Long,
    val name: String,
    val id: String,
    val puuid: String,
    val summonerLevel: Long
)
