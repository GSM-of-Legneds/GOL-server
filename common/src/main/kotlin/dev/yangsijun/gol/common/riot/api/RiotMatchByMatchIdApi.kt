package dev.yangsijun.gol.common.riot.api

interface RiotMatchByMatchIdApi {
    fun execute(matchId: String, time: Long): Map<String, Any>
}
