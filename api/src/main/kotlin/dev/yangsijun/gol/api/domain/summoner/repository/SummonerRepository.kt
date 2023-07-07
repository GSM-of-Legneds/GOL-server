package dev.yangsijun.gol.api.domain.summoner.repository

import dev.yangsijun.gol.common.entity.summoner.Summoner
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface SummonerRepository: MongoRepository<Summoner, ObjectId> {
    fun findByUserId(id: ObjectId): Summoner?
}
