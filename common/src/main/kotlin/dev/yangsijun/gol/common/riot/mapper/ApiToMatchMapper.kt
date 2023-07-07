package dev.yangsijun.gol.common.riot.mapper

import dev.yangsijun.gol.common.entity.match.Match
import dev.yangsijun.gol.common.entity.summoner.Summoner
import org.bson.types.ObjectId

class ApiToMatchMapper {

    companion object {
        fun responseToMatch(id: ObjectId?, response: Map<String, Any>, summoners: List<Summoner>): Match {
            return Match(id, response, summoners)
        }
    }
}
