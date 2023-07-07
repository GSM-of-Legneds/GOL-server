package dev.yangsijun.gol.common.riot.mapper

import dev.yangsijun.gol.common.entity.summoner.Summoner
import dev.yangsijun.gol.common.entity.user.User
import dev.yangsijun.gol.common.riot.dto.SummonerApiResponse
import org.bson.types.ObjectId

class ApiToSummonerMapper {

    companion object {
        fun responseToSummoner(id: ObjectId?, response: SummonerApiResponse, user: User): Summoner {
            return Summoner(
                id = id,
                user = user,
                userId = user.id!!,
                summonerId = response.id,
                accountId = response.accountId,
                puuid = response.puuid,
                name = response.name,
                profileIconId = response.profileIconId,
                revisionDate = response.revisionDate,
                summonerLevel = response.summonerLevel
            )
        }
    }
}
