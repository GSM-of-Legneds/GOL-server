package dev.yangsijun.gol.common.riot.api.impl

import dev.yangsijun.gol.common.logger.LoggerDelegator
import dev.yangsijun.gol.common.riot.api.RiotApiService
import dev.yangsijun.gol.common.riot.api.RiotMatchIdsByPuuidApi
import dev.yangsijun.gol.common.riot.common.RiotApiManager
import dev.yangsijun.gol.common.riot.common.RiotApiUtil
import dev.yangsijun.gol.common.riot.dto.MatchIdsApiRequest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class RiotMatchIdsByPuuidApiImpl(
    val riotApiManager: RiotApiManager,
    val restTemplate: RestTemplate
) : RiotApiService<List<String>, MatchIdsApiRequest>(), RiotMatchIdsByPuuidApi {
    val log by LoggerDelegator()
    override fun execute(request: MatchIdsApiRequest, time: Long): List<String> {
        return this.sendRequest(request, time)
    }

    override fun apiCall(pathVariable: MatchIdsApiRequest): List<String>? {
        val requestEntity: HttpEntity<Any> = HttpEntity(riotApiManager.getHeaders())
        log.debug("#apiCall({}) requestEntity : {}", pathVariable, requestEntity)
        val responseType = object : ParameterizedTypeReference<List<String>>() {}
        val responseEntity: ResponseEntity<List<String>> = restTemplate.exchange(
            this.getUrl(pathVariable),
            HttpMethod.GET,
            requestEntity,
            responseType
        )
        return responseEntity.body
    }

    override fun getUrl(pathVariable: MatchIdsApiRequest): String {
        val params: String =
            addParam(pathVariable.start, pathVariable.count, pathVariable.startTime, pathVariable.endTime)
        val uri: String =
            RiotApiUtil.ASIA_HOST +
                    RiotApiUtil.MatchIds.BY_PUUID_URL_PREFIX +
                    pathVariable.puuid +
                    RiotApiUtil.MatchIds.BY_PUUID_URL_POSTFIX +
                    params
        log.debug("#getUrl({}) uri : {}", pathVariable, uri)
        return uri
    }


    fun addParam(
        start: Int,
        count: Int,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): String {
        val uriBuilder: UriComponentsBuilder = UriComponentsBuilder.newInstance()

        log.debug(
            "#addParam( start: {}, count: {}, startTime: {}, endTime: {} )",
            start,
            count,
            startTime,
            endTime
        )

        val epochStartTime = startTime.toEpochSecond(ZoneOffset.UTC)
        val epochEndTime = endTime.toEpochSecond(ZoneOffset.UTC)

        uriBuilder.queryParam("startTime", epochStartTime.toString())
        log.debug("#addParam - startTime: {}", epochStartTime)

        uriBuilder.queryParam("endTime", epochEndTime)
        log.debug("#addParam - endTime: {}", epochEndTime)

        uriBuilder.queryParam("start", start)
        uriBuilder.queryParam("count", count)

        val uriPath: String = uriBuilder.build().toString()
        log.debug("#addParam - uriPath: {}", uriPath)

        return uriPath
    }
}
