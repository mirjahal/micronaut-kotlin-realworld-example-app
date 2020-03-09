package io.realworld.app.micronaut.domain.business.impl

import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.UserDetails
import io.micronaut.security.authentication.providers.PasswordEncoder
import io.micronaut.security.token.jwt.generator.AccessRefreshTokenGenerator;
import io.realworld.app.micronaut.domain.business.AuthenticationBusiness
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.repository.UserRepository
import javax.inject.Singleton

@Singleton
class AuthenticationBusinessImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private var accessRefreshTokenGenerator: AccessRefreshTokenGenerator
) : AuthenticationBusiness {

    override fun authenticate(email: String, rawPassword: String): User {
        val user = findUserByCredentials(email, rawPassword).apply {
            token = getAccessToken(this)
        }

        return userRepository.update(user)
    }

    private fun findUserByCredentials(email: String, rawPassword: String): User {
        val user = userRepository
            .findByEmail(email)
            .orElseThrow { AuthenticationException() }

        if (passwordsDoesNotMatch(rawPassword, user.password)) {
            throw AuthenticationException()
        }

        return user
    }

    private fun getAccessToken(user: User): String {
        val accessRefreshToken = accessRefreshTokenGenerator
            .generate(UserDetails(user.id.toString(), listOf()))
            .orElseThrow { AuthenticationException() }

        return accessRefreshToken.accessToken
    }

    private fun passwordsDoesNotMatch(rawPassword: String, encodedPassword: String) = passwordEncoder
        .matches(rawPassword, encodedPassword)
        .not()

}