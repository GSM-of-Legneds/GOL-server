package dev.yangsijun.gol.api.global.gauth

import dev.yangsijun.gauth.core.GAuthAuthenticationException
import dev.yangsijun.gauth.core.user.DefaultGAuthUser
import dev.yangsijun.gauth.core.user.GAuthUser
import dev.yangsijun.gauth.userinfo.DefaultGAuthUserService
import dev.yangsijun.gauth.userinfo.GAuthAuthorizationRequest
import dev.yangsijun.gol.api.domain.user.repository.TokenRepository
import dev.yangsijun.gol.common.entity.token.Token
import gauth.GAuth
import gauth.GAuthToken
import gauth.GAuthUserInfo
import gauth.exception.GAuthException
import org.springframework.http.HttpStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.io.IOException
import java.util.List


class CustomGAuthService(
    val gAuth: GAuth,
    val tokenRepository: TokenRepository,
) : DefaultGAuthUserService(gAuth) {

    val GAUTH_CLIENT_CODE = "gauth_client_error"

    val GAUTH_SERVER_CODE = "gauth_server_error"

    override fun loadUser(userRequest: GAuthAuthorizationRequest): GAuthUser? {
        val token = getToken(userRequest)

        val info: GAuthUserInfo
        info = try {
            gAuth.getUserInfo(token.getAccessToken())
        } catch (e: GAuthException) {
            throw handleGAuthException(e)
        } catch (e: IOException) {
            throw GAuthAuthenticationException(
                GAUTH_SERVER_CODE,
                "Something wrong access to GAuth Authorization Sever", e.cause
            )
        }

        // gauth 사용자 토큰 저장
        tokenRepository.save(Token(null, info.email, token.accessToken, token.refreshToken))

        return getGAuthUser(info, token)
    }

    private fun getToken(request: GAuthAuthorizationRequest): GAuthToken {
        val registration = request.getgauthRegistration()
        val code = request.code
        val clientId = registration.clientId
        val clientSecret = registration.clientSecret
        val redirectUri = registration.redirectUri
        val token: GAuthToken
        token = try {
            gAuth.generateToken(code, clientId, clientSecret, redirectUri)
        } catch (e: GAuthException) {
            throw handleGAuthException(e)
        } catch (e: IOException) {
            throw GAuthAuthenticationException(
                GAUTH_SERVER_CODE,
                "Something wrong access to GAuth Authorization Sever", e.cause
            )
        }
        return token
    }

    private fun getGAuthUser(info: GAuthUserInfo, token: GAuthToken): GAuthUser {
        val nameAttribute = "email"
        val email = info.email
        val name = info.name
        val grade = info.grade
        val classNum = info.classNum
        val num = info.num
        val gender = info.gender
        val profileUrl = info.profileUrl
        val role = info.role
        val attributes: Map<String, Any> = HashMap<String, Any>(
            java.util.Map.of(
                nameAttribute, email,
                "name", name,
                "grade", grade,
                "classNum", classNum,
                "num", num,
                "gender", gender,
                "profileUrl", profileUrl,
                "role", role
            )
        )
        val authorities: Collection<GrantedAuthority> =
            ArrayList<GrantedAuthority>(List.of(SimpleGrantedAuthority(role)))
        return DefaultGAuthUser(authorities, attributes, nameAttribute, token)
    }

    private fun handleGAuthException(ex: GAuthException): Throwable {
        return if (is4xx(ex.code)) {
            GAuthAuthenticationException(
                GAUTH_CLIENT_CODE,
                "[GAuth Authorization Server Status Code : " + HttpStatus.valueOf(ex.code).value() + "] ",
                ex.cause
            )
        } else {
            GAuthAuthenticationException(
                GAUTH_SERVER_CODE,
                "[GAuth Authorization Server Status Code : " + HttpStatus.valueOf(ex.code).value() + "] ",
                ex.cause
            )
        }
    }

    private fun is4xx(code: Int): Boolean {
        return code < 500
    }

}
