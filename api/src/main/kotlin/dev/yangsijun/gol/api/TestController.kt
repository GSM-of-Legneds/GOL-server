package dev.yangsijun.gol.api

import dev.yangsijun.gol.common.riot.api.*
import dev.yangsijun.gol.common.riot.dto.LeagueApiResponse
import dev.yangsijun.gol.common.riot.dto.MatchIdsApiRequest
import dev.yangsijun.gol.common.riot.dto.SummonerApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RestController
class TestController(
    val riotSummonerByNameApi: RiotSummonerByNameApi,
    val riotSummonerByPuuidApi: RiotSummonerByPuuidApi,
    val riotMatchByMatchIdApi: RiotMatchByMatchIdApi,
    val riotMatchIdsByPuuidApi: RiotMatchIdsByPuuidApi,
    val riotLeagueBySummonerIdApi: RiotLeagueBySummonerIdApi
) {
    @GetMapping("/s-name")
    fun sname(): SummonerApiResponse {
        return riotSummonerByNameApi.execute("골드1", 0)
    }

    @GetMapping("/s-id")
    fun sid(): SummonerApiResponse {
        return riotSummonerByPuuidApi.execute(
            "JRv9GZ1NllHPUY1DXqQZ66yWwbDNIdi8UDeOtW-4pFxPQMhr17Vc5x1yrhWFehSvyeP2sU3rWiSO2g",
            0
        )
    }

    @GetMapping("m-id")
    fun mids(): Map<String, Any> {
        return riotMatchByMatchIdApi.execute(
            "KR_6573502974",
            0
        )
    }

    @GetMapping("m-ids")
    fun mid(): List<String> {
        return riotMatchIdsByPuuidApi.execute(
            MatchIdsApiRequest(
                "JRv9GZ1NllHPUY1DXqQZ66yWwbDNIdi8UDeOtW-4pFxPQMhr17Vc5x1yrhWFehSvyeP2sU3rWiSO2g",
                0,
                100,
                LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now(), LocalTime.MAX),
            ),
            0
        )
    }

    @GetMapping("l-id")
    fun lid(): List<LeagueApiResponse> {
        return riotLeagueBySummonerIdApi.execute("DIiLDPb8BjQewHIbqm1adVUIAObCRiA-wHgAU7mKaGjRNgI", 0)
    }
}
