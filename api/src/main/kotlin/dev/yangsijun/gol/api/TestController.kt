package dev.yangsijun.gol.api

import dev.yangsijun.gol.common.entity.summoner.Summoner
import dev.yangsijun.gol.common.entity.user.User
import dev.yangsijun.gol.common.logger.LoggerDelegator
import dev.yangsijun.gol.common.riot.api.*
import dev.yangsijun.gol.common.riot.dto.LeagueApiResponse
import dev.yangsijun.gol.common.riot.dto.MatchIdsApiRequest
import dev.yangsijun.gol.common.riot.dto.SummonerApiResponse
import dev.yangsijun.gol.common.riot.mapper.ApiToLeagueMapper
import dev.yangsijun.gol.common.riot.mapper.ApiToMatchMapper
import dev.yangsijun.gol.common.riot.mapper.ApiToSummonerMapper
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

val gauthInfo: Map<String, Any> = mapOf(
    "email" to "asdasd",
    "name" to "양시준",
    "grade" to 3,
    "classNum" to 3,
    "num" to 13,
    "gender" to "male",
    "profileUrl" to "adasdasd",
    "role" to "User"
)
val user: User = User(
    ObjectId(TestRunner.userId),
    email = gauthInfo["email"] as String,
    name = gauthInfo["name"] as String,
    grade = gauthInfo["grade"] as Int,
    classNum = gauthInfo["classNum"] as Int,
    num = gauthInfo["num"] as Int,
    gender = gauthInfo["gender"] as String,
    profileUrl = gauthInfo["profileUrl"] as String,
    role = gauthInfo["role"] as String
)
val summoner: Summoner = Summoner(
    id = ObjectId(TestRunner.userId),
    userId = user.id!!,
    user = user,
    summonerId = "P893mr8WYQG6ITMjD-xj-7PE7j3aL87aXkjmq3t4G9wDCX0",
    accountId = "yVBihO_Uu-t64I1MCSgq2MBwOy9Tzko9iFzJiAethx_u",
    puuid = "54mQFtsBR1DAqJbanmn1JFKXQVnQ90Rvu_lxocLvc7c_cd8N8bFcL4FXJJ4FgPARtkZugXNmCgh92A",
    name = "Facker",
    profileIconId = 4832,
    revisionDate = 1688249434487,
    summonerLevel = 730
)

@RestController
class TestController(
    val riotSummonerByNameApi: RiotSummonerByNameApi,
    val riotSummonerByPuuidApi: RiotSummonerByPuuidApi,
    val riotMatchByMatchIdApi: RiotMatchByMatchIdApi,
    val riotMatchIdsByPuuidApi: RiotMatchIdsByPuuidApi,
    val riotLeagueBySummonerIdApi: RiotLeagueBySummonerIdApi
) {
    val log by LoggerDelegator()

    @GetMapping("/s-name")
    fun sname(): SummonerApiResponse {
        return riotSummonerByNameApi.execute("골드1", 0)
    }

    @GetMapping("/s-id")
    fun sid(): SummonerApiResponse {
        val response = riotSummonerByPuuidApi.execute(
            "JRv9GZ1NllHPUY1DXqQZ66yWwbDNIdi8UDeOtW-4pFxPQMhr17Vc5x1yrhWFehSvyeP2sU3rWiSO2g",
            0
        )
        log.debug("#sid : {}", ApiToSummonerMapper.responseToSummoner(id = null, response = response, user))
        return response
    }

    @GetMapping("m-id")
    fun mid(): Map<String, Any> {
        val response = riotMatchByMatchIdApi.execute(
            "KR_6573502974",
            0
        )
        log.debug("#mid : {}", ApiToMatchMapper.responseToMatch(id = null, response = response, listOf(summoner)))
        return response
    }

    @GetMapping("m-ids")
    fun mids(): List<String> {
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
        val responses = riotLeagueBySummonerIdApi.execute("DIiLDPb8BjQewHIbqm1adVUIAObCRiA-wHgAU7mKaGjRNgI", 0)
        responses.forEach {
            log.debug("#lid : {}", ApiToLeagueMapper.responseToLeague(id = null, response = it, summoner))
        }
        return responses
    }
}
