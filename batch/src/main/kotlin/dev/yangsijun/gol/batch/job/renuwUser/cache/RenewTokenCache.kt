package dev.yangsijun.gol.batch.job.renuwUser.cache

import dev.yangsijun.gol.common.entity.token.Token
import dev.yangsijun.gol.common.logger.LoggerDelegator

open class RenewTokenCache {

    companion object {
        private val log by LoggerDelegator()
        private val cache: MutableMap<String, Token> = mutableMapOf()
    }

    fun put(email: String, token: Token) {
        cache[email] = token
    }

    fun get(email: String): Token? {
        log.trace("keys : {}", cache.keys)
        log.trace("values : {}", cache.values)
        return cache.get(email)
    }

    fun clear() {
        cache.clear()
    }
}
