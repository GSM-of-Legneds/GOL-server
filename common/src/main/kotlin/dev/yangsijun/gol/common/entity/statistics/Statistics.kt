package dev.yangsijun.gol.common.entity.statistics

import dev.yangsijun.gol.common.common.entity.BaseTimeEntity
import dev.yangsijun.gol.common.common.util.GolObjectUtils
import dev.yangsijun.gol.common.entity.summoner.Summoner
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document

@Document
@CompoundIndex(name = "cmp-idx-by_id", def = "{'summoners.summoner.userId': 1, 'summoners.id': 1, 'createdDate': -1}", unique = true)
@CompoundIndex(name = "cmp-idx-by_data", def = "{'createdDate': -1}", unique = true)
class Statistics(
    @Id var id: ObjectId? = null,
    val summoner: Summoner,
    val maxWinStreak: Int = 0,
    val maxLoseStreak: Int = 0,
    val curWinStreak: Int = 0,
    val curLoseStreak: Int = 0,
    val winCount: Int = 0,
    val loseCount: Int = 0
    // 나중에 계속 필드 추가할 수 있음
): BaseTimeEntity() {
    override fun toString() = GolObjectUtils.reflectionToString(this)
}
