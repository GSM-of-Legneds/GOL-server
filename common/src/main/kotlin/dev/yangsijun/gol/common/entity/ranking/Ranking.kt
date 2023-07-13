package dev.yangsijun.gol.common.entity.ranking


import dev.yangsijun.gol.common.common.enums.RankingType
import dev.yangsijun.gol.common.common.util.GolObjectUtils
import dev.yangsijun.gol.common.entity.summoner.Summoner
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document

@Document
@CompoundIndex(name = "cmp-idx-by_id", def = "{'summoners.summoner.userId': 1, 'summoners.id': 1, 'createdDate': -1}", unique = true)
@CompoundIndex(name = "cmp-idx-by_data", def = "{'createdDate': -1}", unique = true)
class Ranking(
    @Id var id: ObjectId? = null,
    val type: RankingType,
    val place: Int, // n 순위
    val value: String, // 점수
    val additional: Map<String, Any> = emptyMap(), // 아무거나 더 필요한 자료
    val summoner: Summoner
) {
    override fun toString() = GolObjectUtils.reflectionToString(this)
}
