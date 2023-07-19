package dev.yangsijun.gol.api.domain.user

import dev.yangsijun.gol.common.entity.statistics.Statistics
import dev.yangsijun.gol.common.entity.summoner.Summoner
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface StatisticsRepository: MongoRepository<Statistics, ObjectId> {
}
