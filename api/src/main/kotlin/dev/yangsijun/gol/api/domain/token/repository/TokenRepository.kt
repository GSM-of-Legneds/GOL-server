package dev.yangsijun.gol.api.domain.user.repository

import dev.yangsijun.gol.common.entity.token.Token
import dev.yangsijun.gol.common.entity.user.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface TokenRepository: MongoRepository<Token, ObjectId> {
    fun findByGauthEmail(email: String): Token?
}
