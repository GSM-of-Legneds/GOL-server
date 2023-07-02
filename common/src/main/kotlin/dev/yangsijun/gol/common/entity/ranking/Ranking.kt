package dev.yangsijun.gol.common.entity.ranking

import dev.yangsijun.gol.common.common.entity.BaseTimeEntity
import dev.yangsijun.gol.common.common.enums.RankingType
import dev.yangsijun.gol.common.entity.summoner.Summoner
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Ranking(
    @Id val id: ObjectId? = null,
    val type: RankingType,
    val place: Int, // n 순위
    val value: String, // 점수
    val additional: Map<String, Any> = emptyMap(), // 아무거나 더 필요한 자료
    val summoner: Summoner
): BaseTimeEntity() {
}
