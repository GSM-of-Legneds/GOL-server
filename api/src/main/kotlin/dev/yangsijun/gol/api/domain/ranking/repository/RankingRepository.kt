package dev.yangsijun.gol.api.domain.ranking.repository

import dev.yangsijun.gol.common.entity.ranking.Ranking
import dev.yangsijun.gol.common.entity.summoner.Summoner
import dev.yangsijun.gol.common.entity.user.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface RankingRepository: MongoRepository<Ranking, ObjectId> {
    fun findBySummonerOrderByCreatedDateDesc(summoner: Summoner): Ranking?
}
