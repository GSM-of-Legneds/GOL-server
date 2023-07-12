package dev.yangsijun.gol.common.entity.token

import dev.yangsijun.gol.common.common.entity.BaseTimeEntity
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Token(
    @Id var id: ObjectId? = null,
    val gauthEmail: String,
    val accessToken: String,
    val refreshToken: String
): BaseTimeEntity()
