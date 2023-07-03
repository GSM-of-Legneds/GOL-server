package dev.yangsijun.gol.common.riot.api.impl

import dev.yangsijun.gol.common.logger.LoggerDelegator
import dev.yangsijun.gol.common.riot.api.RiotApiService
import dev.yangsijun.gol.common.riot.api.RiotLeagueBySummonerIdApi
import dev.yangsijun.gol.common.riot.common.RiotApiManager
import dev.yangsijun.gol.common.riot.common.RiotApiUtil
import dev.yangsijun.gol.common.riot.dto.LeagueApiResponse
import dev.yangsijun.gol.common.riot.dto.SummonerApiResponse
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class RiotLeagueBySummonerIdApiImpl(
    val riotApiManager: RiotApiManager,
    val restTemplate: RestTemplate
) : RiotApiService<List<LeagueApiResponse>, String>(), RiotLeagueBySummonerIdApi {
    val log by LoggerDelegator()

    override fun execute(summonerId: String, time: Long): List<LeagueApiResponse> {
        return this.sendRequest(summonerId, time)
    }

    override fun apiCall(pathVariable: String): List<LeagueApiResponse>? {
        val requestEntity: HttpEntity<Any> = HttpEntity(riotApiManager.getHeaders())
        log.debug("#apiCall({}) requestEntity : {}", pathVariable, requestEntity)
        val responseType = object : ParameterizedTypeReference<List<LeagueApiResponse>>() {}
        val responseEntity: ResponseEntity<List<LeagueApiResponse>> = restTemplate.exchange(
            this.getUrl(pathVariable),
            HttpMethod.GET,
            requestEntity,
            responseType
        )
        return responseEntity.body
    }

    override fun getUrl(pathVariable: String): String {
        val uri: String = RiotApiUtil.KR_HOST +
                RiotApiUtil.League.BY_SUMMONER_ID_URL_PREFIX +
                pathVariable
        log.debug("#getUrl({}) uri : {}", pathVariable, uri)
        return uri
    }
}
