package dev.yangsijun.gol.common.entity.league

import dev.yangsijun.gol.common.common.entity.BaseTimeEntity
import dev.yangsijun.gol.common.common.enums.game.RankedType
import dev.yangsijun.gol.common.common.enums.game.GameRankType
import dev.yangsijun.gol.common.common.enums.game.TierType
import dev.yangsijun.gol.common.common.util.GolObjectUtils
import dev.yangsijun.gol.common.entity.summoner.Summoner
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document

@Document
@CompoundIndex(name = "cmp-idx-by_id", def = "{'summoners.summoner.userId': 1, 'summoners.id': 1, 'createdDate': -1}", unique = true)
@CompoundIndex(name = "cmp-idx-by_data", def = "{'createdDate': -1}", unique = true)
class League(
    @Id var id: ObjectId? = null,
    val summoner: Summoner,
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
): BaseTimeEntity() {
    override fun toString() = GolObjectUtils.reflectionToString(this)
}
