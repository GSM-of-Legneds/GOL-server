package dev.yangsijun.gol.common.riot.api

import dev.yangsijun.gol.common.riot.exception.RiotException
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import java.net.SocketException

abstract class RiotApiService<T, V> {

    protected abstract fun apiCall(pathVariable: V): T? // 예외처리를 전부 여기서 하려고 nullable 했는데, 굳이 그럴 필요가 있었을까?
    protected abstract fun getUrl(pathVariable: V): String

    fun sendRequest(pathVariable: V, waitTime: Long): T {
        Thread.sleep(waitTime) // millisecond 단위
        return try {
            apiCall(pathVariable)
                ?: throw RuntimeException("Returned null") // 의도하지 않은 상황
        } catch (ex: HttpClientErrorException) {
            val status: HttpStatus = ex.statusCode as HttpStatus
            when (ex.statusCode) {
                HttpStatus.NOT_FOUND -> {
                    throw RiotException("API 호출 중 에러 발생 ${status.value()}", status, ex)
                }
                HttpStatus.UNAUTHORIZED -> {
                    throw RiotException("API 호출 중 에러 발생 ${status.value()}", status, ex)
                }
                HttpStatus.FORBIDDEN -> {
                    throw RiotException("API 호출 중 에러 발생 ${status.value()}", status, ex)
                }
                else -> {
                    throw RiotException("API 호출 중 에러 발생 ${status.value()}", status, ex)
                }
            }
        }
    }
}
