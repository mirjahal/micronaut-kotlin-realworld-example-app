package io.realworld.app.micronaut.infrastructure.providers

import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.UserDetails
import io.micronaut.security.token.jwt.generator.AccessRefreshTokenGenerator
import io.realworld.app.micronaut.application.providers.AccessTokenProvider
import javax.inject.Singleton

@Singleton
class JwtTokenProvider(
    private val accessRefreshTokenGenerator: AccessRefreshTokenGenerator
) : AccessTokenProvider {

    override fun generate(subject: String): String {
        val accessRefreshToken = accessRefreshTokenGenerator
            .generate(UserDetails(subject, listOf()))
            .orElseThrow { AuthenticationException() }

        return accessRefreshToken.accessToken
    }

}