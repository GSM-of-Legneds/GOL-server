package dev.yangsijun.gol.common.entity.user

import dev.yangsijun.gol.common.common.entity.BaseTimeEntity
import dev.yangsijun.gol.common.common.util.GolObjectUtils
import gauth.GAuthUserInfo
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class User(
    @Id var id: ObjectId? = null,
    val info: GAuthUserInfo,
    val removed: Boolean = false
): BaseTimeEntity() {
    override fun toString() = GolObjectUtils.reflectionToString(this)
}
