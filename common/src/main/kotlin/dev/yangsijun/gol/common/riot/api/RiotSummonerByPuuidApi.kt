package dev.yangsijun.gol.common.riot.api

import dev.yangsijun.gol.common.riot.dto.SummonerApiResponse

interface RiotSummonerByPuuidApi {
    fun execute(puuid: String, time: Long): SummonerApiResponse
}
