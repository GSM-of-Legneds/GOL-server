package dev.yangsijun.gol.common.riot.api.impl


import dev.yangsijun.gol.common.logger.LoggerDelegator
import dev.yangsijun.gol.common.riot.api.RiotApiService
import dev.yangsijun.gol.common.riot.api.RiotSummonerByPuuidApi
import dev.yangsijun.gol.common.riot.common.RiotApiUtil
import dev.yangsijun.gol.common.riot.common.RiotApiManager
import dev.yangsijun.gol.common.riot.dto.SummonerApiResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
class RiotSummonerByPuuidApiImpl(
    val riotApiManager: RiotApiManager,
    val restTemplate: RestTemplate
) : RiotApiService<SummonerApiResponse, String>(), RiotSummonerByPuuidApi {
    val log by LoggerDelegator()

    override fun execute(puuid: String, time: Long): SummonerApiResponse {
        return this.sendRequest(puuid, time)
    }

    override fun getUrl(pathVariable: String): String {
        val uri: String = RiotApiUtil.KR_HOST +
                RiotApiUtil.Summoner.BY_PUUID_URL_PREFIX +
                pathVariable
        log.debug("[RiotSummonerByNameApiImpl#getUrl] uri : {}", uri)
        return uri
    }

    override fun apiCall(pathVariable: String): SummonerApiResponse? {
        val requestEntity: HttpEntity<Any> = HttpEntity(riotApiManager.getHeaders())
        log.debug("[RiotSummonerByNameApiImpl#apiCall] requestEntity : {}", requestEntity)
        val responseEntity: ResponseEntity<SummonerApiResponse> = restTemplate.exchange(
            getUrl(pathVariable),
            HttpMethod.GET,
            requestEntity,
            SummonerApiResponse::class.java
        )
        return responseEntity.body
    }

}
