package dev.yangsijun.gol.common.riot.api.impl


import dev.yangsijun.gol.common.logger.LoggerDelegator
import dev.yangsijun.gol.common.riot.api.RiotApiService
import dev.yangsijun.gol.common.riot.api.RiotSummonerByNameApi
import dev.yangsijun.gol.common.riot.common.RiotApiManager
import dev.yangsijun.gol.common.riot.common.RiotApiUtil
import dev.yangsijun.gol.common.riot.dto.SummonerApiResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
class RiotSummonerByNameApiImpl(
    val riotApiManager: RiotApiManager,
    val restTemplate: RestTemplate
) : RiotApiService<SummonerApiResponse, String>(), RiotSummonerByNameApi {
    val log by LoggerDelegator()

    override fun execute(name: String, time: Long): SummonerApiResponse {
        return this.sendRequest(name, time)
    }

    override fun getUrl(pathVariable: String): String {
        val uri: String = RiotApiUtil.KR_HOST +
                RiotApiUtil.Summoner.BY_NAME_URL_PREFIX +
                pathVariable
        log.debug("#getUrl({}) uri : {}", pathVariable, uri)
        return uri
    }

    override fun apiCall(pathVariable: String): SummonerApiResponse? {
        val requestEntity: HttpEntity<Any> = HttpEntity(riotApiManager.getHeaders())
        log.debug("#apiCall({}) requestEntity : {}", pathVariable, requestEntity)
        val responseEntity: ResponseEntity<SummonerApiResponse>
        responseEntity = restTemplate.exchange(
            getUrl(pathVariable),
            HttpMethod.GET,
            requestEntity,
            SummonerApiResponse::class.java
        )
        return responseEntity.body
    }

}
