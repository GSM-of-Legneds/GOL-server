package dev.yangsijun.gol.api.domain.match

import dev.yangsijun.gol.common.entity.match.Match
import dev.yangsijun.gol.common.entity.summoner.Summoner
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface MatchRepository: MongoRepository<Match, ObjectId> {
    fun findBySummonersInOrderByCreatedDateDesc(summoners: MutableCollection<MutableList<Summoner>>): Match?
}
