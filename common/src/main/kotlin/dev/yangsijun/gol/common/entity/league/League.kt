package dev.yangsijun.gol.common.entity.league

import dev.yangsijun.gol.common.common.entity.BaseTimeEntity
import dev.yangsijun.gol.common.common.enums.game.RankedType
import dev.yangsijun.gol.common.common.enums.game.GameRankType
import dev.yangsijun.gol.common.common.enums.game.TierType
import dev.yangsijun.gol.common.entity.summoner.Summoner
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class League(
    @Id val id: ObjectId? = null,
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
}
