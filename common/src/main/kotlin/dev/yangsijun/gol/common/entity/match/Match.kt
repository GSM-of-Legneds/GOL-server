package dev.yangsijun.gol.common.entity.match

import dev.yangsijun.gol.common.common.entity.BaseTimeEntity
import dev.yangsijun.gol.common.entity.summoner.Summoner
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Match(
    @Id val id: ObjectId? = null,
    val data: Map<String, Any>,
    val summoners: List<Summoner>
): BaseTimeEntity() {
}
