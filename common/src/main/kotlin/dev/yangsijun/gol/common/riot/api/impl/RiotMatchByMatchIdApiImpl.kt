package dev.yangsijun.gol.common.riot.api.impl

import dev.yangsijun.gol.common.logger.LoggerDelegator
import dev.yangsijun.gol.common.riot.api.RiotApiService
import dev.yangsijun.gol.common.riot.api.RiotMatchByMatchIdApi
import dev.yangsijun.gol.common.riot.common.RiotApiManager
import dev.yangsijun.gol.common.riot.common.RiotApiUtil
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class RiotMatchByMatchIdApiImpl(
    val riotApiManager: RiotApiManager,
    val restTemplate: RestTemplate
) : RiotApiService<Map<String, Any>, String>(), RiotMatchByMatchIdApi {
    val log by LoggerDelegator()

    var uriBuilder = UriComponentsBuilder.newInstance()

    override fun execute(matchId: String, time: Long): Map<String, Any> {
        return this.sendRequest(matchId, time)
    }

    override fun apiCall(pathVariable: String): Map<String, Any>? {
        val requestEntity: HttpEntity<Any> = HttpEntity(riotApiManager.getHeaders())
        log.debug("#apiCall({}) requestEntity : {}", pathVariable, requestEntity)
        val responseType = object : ParameterizedTypeReference<Map<String, Any>>() {}
        val responseEntity: ResponseEntity<Map<String, Any>> = restTemplate.exchange(
            this.getUrl(pathVariable),
            HttpMethod.GET,
            requestEntity,
            responseType
        )
        return responseEntity.body
    }

    override fun getUrl(pathVariable: String): String {
        val uri: String = RiotApiUtil.ASIA_HOST +
                RiotApiUtil.Match.BY_MATCH_ID_PREFIX +
                pathVariable
        log.debug("#getUrl({}) uri : {}", pathVariable, uri)
        return uri
    }
}
