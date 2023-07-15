package dev.yangsijun.gol.common.entity.summoner


import dev.yangsijun.gol.common.common.util.GolObjectUtils
import dev.yangsijun.gol.common.entity.user.User
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.Field

@Document
// puuid 인덱스 추가
class Summoner(
    @Id var id: ObjectId? = null,
    @DocumentReference
    @Field(name = "userId")
    val user: User,
    val summonerId: String,
    val accountId: String,
    val puuid: String,
    val name: String,
    val profileIconId: Int,
    val revisionDate: Long,
    val summonerLevel: Long
) {
    override fun toString() = GolObjectUtils.reflectionToString(this)
}
