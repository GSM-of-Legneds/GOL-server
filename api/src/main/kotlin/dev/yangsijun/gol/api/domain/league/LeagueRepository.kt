package dev.yangsijun.gol.api.domain.summoner

import dev.yangsijun.gol.common.entity.league.League
import dev.yangsijun.gol.common.entity.summoner.Summoner
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface LeagueRepository: MongoRepository<League, ObjectId> {
    fun findBySummonerOrderByCreatedDateDesc(summoner: Summoner): League?
}
