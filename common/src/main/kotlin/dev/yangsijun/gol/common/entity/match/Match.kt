package dev.yangsijun.gol.common.entity.match

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
@CompoundIndex(name = "cmp-idx-by_match", def = "{'data.metadata.matchId': -1}", unique = true)
class Match(
    @Id var id: ObjectId? = null,
    val data: Map<String, Any>,
    val summoners: List<Summoner>
): BaseTimeEntity() {
    override fun toString() = GolObjectUtils.reflectionToString(this)
}
