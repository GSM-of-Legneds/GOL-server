package dev.yangsijun.gol.api.global.gauth

import dev.yangsijun.gauth.authentication.GAuthAuthenticationProvider
import dev.yangsijun.gauth.autoconfigure.GAuthProperties
import dev.yangsijun.gauth.configurer.GAuthLoginConfigurer
import dev.yangsijun.gauth.core.user.GAuthUser
import dev.yangsijun.gauth.registration.GAuthRegistration
import dev.yangsijun.gauth.userinfo.GAuthAuthorizationRequest
import dev.yangsijun.gauth.userinfo.GAuthUserService
import dev.yangsijun.gauth.web.GAuthAuthenticationEntryPoint
import dev.yangsijun.gol.api.domain.user.repository.TokenRepository
import gauth.GAuth
import gauth.impl.GAuthImpl
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager

@Configuration
@EnableConfigurationProperties(GAuthProperties::class)
class GAuthConfig(
    val tokenRepository: TokenRepository,
    val properties: GAuthProperties
) {
    @Bean
    fun autoGAuthUserService(): GAuthUserService<GAuthAuthorizationRequest, GAuthUser>? {
        return CustomGAuthService(gauth()!!, tokenRepository)
    }

    @Bean
    fun gauth(): GAuth? {
        return GAuthImpl()
    }

    @Bean
    fun autoGAuthRegistration(): GAuthRegistration? {
        return GAuthRegistration(properties!!.clientId(), properties!!.clientSecret(), properties!!.redirectUri())
    }

    @Bean
    fun autoGAuthAuthenticationConfigurer(): GAuthLoginConfigurer<*> {
        return GAuthLoginConfigurer(autoGAuthRegistration(), autoAuthenticationManager())
    }

    @Bean
    fun autoAuthenticationManager(): AuthenticationManager? {
        return ProviderManager(
            autoGAuthAuthenticationProvider()
        )
    }

    @Bean
    fun autoGAuthAuthenticationProvider(): GAuthAuthenticationProvider? {
        return GAuthAuthenticationProvider(autoGAuthUserService())
    }

    @Bean
    fun autoGAuthAuthenticationEntryPoint(): GAuthAuthenticationEntryPoint? {
        return GAuthAuthenticationEntryPoint()
    }
}
