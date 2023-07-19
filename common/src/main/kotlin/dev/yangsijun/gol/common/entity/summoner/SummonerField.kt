package dev.yangsijun.gol.common.entity.summoner


import dev.yangsijun.gol.common.common.util.GolObjectUtils
import dev.yangsijun.gol.common.entity.user.User
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.Field

/*
 * 비정규화된 Summoner를 저장하는 경우 엔티티 대신 해당 객체를 사용한다.
 *
*/
class SummonerField(
    var id: ObjectId? = null,
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
