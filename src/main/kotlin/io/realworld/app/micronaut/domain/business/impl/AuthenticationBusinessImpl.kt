package io.realworld.app.micronaut.domain.business.impl

import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.providers.PasswordEncoder
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator
import io.realworld.app.micronaut.domain.business.AuthenticationBusiness
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.repository.UserRepository
import javax.inject.Singleton

@Singleton
class AuthenticationBusinessImpl(
    private val userRepository: UserRepository,
    private var jwtTokenGenerator: JwtTokenGenerator,
    private val passwordEncoder: PasswordEncoder
) : AuthenticationBusiness {

    override fun authenticate(email: String, rawPassword: String): User {
        val user = findUserByCredentials(email, rawPassword).apply {
            token = getAccessToken(this)
        }

        return userRepository.update(user)
    }

    private fun findUserByCredentials(email: String, rawPassword: String): User {
        val optionalUser = userRepository.findByEmail(email)
        if (optionalUser.isEmpty) {
            throw AuthenticationException()
        }

        val user = optionalUser.get()
        if (passwordsDoesNotMatch(rawPassword, user.password)) {
            throw AuthenticationException()
        }

        return user
    }

    private fun getAccessToken(user: User): String {
        val claims = mapOf(
            "id" to user.id.toString()
        )

        return jwtTokenGenerator
            .generateToken(claims)
            .orElseThrow { AuthenticationException() }
    }

    private fun passwordsDoesNotMatch(rawPassword: String, encodedPassword: String) = passwordEncoder.matches(rawPassword, encodedPassword).not()

}