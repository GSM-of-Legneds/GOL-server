package dev.yangsijun.gol.common.riot.api

import dev.yangsijun.gol.common.riot.dto.MatchIdsApiRequest

interface RiotMatchIdsByPuuidApi {
    fun execute(request: MatchIdsApiRequest, time: Long): List<String>
}
