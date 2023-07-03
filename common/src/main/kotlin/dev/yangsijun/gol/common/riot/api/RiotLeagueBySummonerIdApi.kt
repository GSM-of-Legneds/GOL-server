package dev.yangsijun.gol.common.riot.api

import dev.yangsijun.gol.common.riot.dto.LeagueApiResponse

interface RiotLeagueBySummonerIdApi {
    fun execute(summonerId: String, time: Long): List<LeagueApiResponse>
}
