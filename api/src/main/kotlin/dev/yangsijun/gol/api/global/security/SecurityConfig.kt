package dev.yangsijun.gol.api.global.security

import dev.yangsijun.gauth.autoconfigure.EnableGAuth
import dev.yangsijun.gauth.configurer.GAuthLoginConfigurer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler


@EnableGAuth
@Configuration
class SecurityConfig(
    val gauth: GAuthLoginConfigurer<HttpSecurity>
) {
    @Value("\${auth.login-success-redirect-uri}") lateinit var loginSuccessUri: String

    @Value("\${auth.login-failure-redirect-uri}") lateinit var loginFailureUri: String


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { cors -> cors.disable() }
            .formLogin { form -> form.disable() }
            .httpBasic { basic -> basic.disable() }
            .apply(gauth
                    .successHandler(SimpleUrlAuthenticationSuccessHandler(loginSuccessUri))
                    .failureHandler(SimpleUrlAuthenticationFailureHandler(loginFailureUri)))
        return http.build()
    }
}
