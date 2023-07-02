package dev.yangsijun.gol.common.entity.user

import dev.yangsijun.gol.common.common.entity.BaseTimeEntity
import gauth.GAuthUserInfo
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class User(
    @Id val id: ObjectId? = null,
    val info: GAuthUserInfo,
    val removed: Boolean = false
): BaseTimeEntity() {
}
