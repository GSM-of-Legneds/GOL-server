package dev.yangsijun.gol.common.entity.league


import dev.yangsijun.gol.common.common.enums.game.RankedType
import dev.yangsijun.gol.common.common.enums.game.GameRankType
import dev.yangsijun.gol.common.common.enums.game.TierType
import dev.yangsijun.gol.common.common.util.GolObjectUtils
import dev.yangsijun.gol.common.entity.summoner.Summoner
import dev.yangsijun.gol.common.entity.summoner.SummonerField
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document

@Document
@CompoundIndex(name = "cmp-idx-by_id", def = "{'summoners.summoner.userId': 1, 'summoners.id': 1, '_id': -1}", unique = true)

class League(
    @Id var id: ObjectId? = null,
    val summoner: SummonerField,
    val leagueId: String,
    val rankedType: RankedType,
    val tierType: TierType,
    val rank: GameRankType,
    val leaguePoints: Int,
    val wins: Int,
    val losses: Int,
    val veteran: Boolean,
    val freshBlood: Boolean,
    val hotStreak: Boolean
) {
    override fun toString() = GolObjectUtils.reflectionToString(this)
}
