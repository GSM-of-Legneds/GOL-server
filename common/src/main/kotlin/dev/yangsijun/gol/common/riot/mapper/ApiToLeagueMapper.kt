package dev.yangsijun.gol.common.riot.mapper

import dev.yangsijun.gol.common.common.enums.game.GameRankType
import dev.yangsijun.gol.common.common.enums.game.RankedType
import dev.yangsijun.gol.common.common.enums.game.TierType
import dev.yangsijun.gol.common.entity.league.League
import dev.yangsijun.gol.common.entity.summoner.Summoner
import dev.yangsijun.gol.common.entity.summoner.SummonerField
import dev.yangsijun.gol.common.riot.dto.LeagueApiResponse
import org.bson.types.ObjectId


class ApiToLeagueMapper {

    companion object {
        fun responseToLeague(id: ObjectId?, response: LeagueApiResponse, summoner: Summoner): League {
            return League(
                id = id,
                summoner = SummonerField.from(summoner),
                leagueId = response.leagueId,
                rankedType = RankedType.valueOf(response.queueType),
                tierType = TierType.valueOf(response.tier),
                rank = GameRankType.valueOf(response.rank),
                leaguePoints = response.leaguePoints,
                wins = response.wins,
                losses = response.losses,
                veteran = response.veteran,
                freshBlood = response.freshBlood,
                hotStreak = response.hotStreak
            )
        }
    }
}
