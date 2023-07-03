package dev.yangsijun.gol.common.riot.config

import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration


@Configuration(proxyBeanMethods = false)
class RestTemplateConfiguration {
    @Bean
    fun restTemplate(configurer: RestTemplateBuilderConfigurer): RestTemplate {
        return configurer.configure(RestTemplateBuilder())
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(3))
            .build()
    }
}
