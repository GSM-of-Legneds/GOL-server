package dev.yangsijun.gol.batch.common.gauth

import gauth.GAuth
import gauth.impl.GAuthImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GAuthConfig {

    @Bean
    fun gauth(): GAuth {
        return GAuthImpl()
    }

}
