package dev.yangsijun.gol.api.global.security

import dev.yangsijun.gauth.autoconfigure.EnableGAuth
import dev.yangsijun.gauth.configurer.GAuthLoginConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain


@EnableGAuth
@Configuration
class SecurityConfig(
    val gauth: GAuthLoginConfigurer<HttpSecurity>
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { cors -> cors.disable() }
            .formLogin { form -> form.disable() }
            .httpBasic { basic -> basic.disable() }
            .apply(gauth)
        return http.build()
    }
}
