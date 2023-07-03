package dev.yangsijun.gol.common.riot.api

import dev.yangsijun.gol.common.riot.dto.SummonerApiResponse

interface RiotSummonerByNameApi {
    fun execute(name: String, time: Long): SummonerApiResponse
}
