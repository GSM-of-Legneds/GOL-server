package dev.yangsijun.gol.batch.job.recordMatch.cache

import dev.yangsijun.gol.common.entity.token.Token
import dev.yangsijun.gol.common.logger.LoggerDelegator

open class RecordMatchCache {

    companion object {
        private val log by LoggerDelegator()
        private val cache: MutableSet<String> = mutableSetOf()
    }

    fun add(ids: String) {
        log.debug("added : {}", ids)
        cache.add(ids)
        log.trace("cache : {}", cache)
    }

    fun clear() {
        cache.clear()
    }
}
