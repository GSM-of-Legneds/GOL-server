package dev.yangsijun.gol.common.entity.summoner

import dev.yangsijun.gol.common.common.entity.BaseTimeEntity
import dev.yangsijun.gol.common.entity.user.User
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Summoner(
    @Id var id: ObjectId? = null,
    val userId: ObjectId,
    val summonerId: String,
    val accountId: String,
    val puuid: String,
    val name: String,
    val profileIconId: Int,
    val revisionDate: Long,
    val summonerLevel: Long
): BaseTimeEntity() {
}
