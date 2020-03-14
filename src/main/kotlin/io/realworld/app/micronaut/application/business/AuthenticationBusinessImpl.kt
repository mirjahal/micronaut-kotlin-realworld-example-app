package io.realworld.app.micronaut.application.business

import io.realworld.app.micronaut.application.exception.InvalidPasswordException
import io.realworld.app.micronaut.application.providers.AccessTokenProvider
import io.realworld.app.micronaut.application.providers.PasswordEncoderProvider
import io.realworld.app.micronaut.domain.business.AuthenticationBusiness
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.entity.User
import javax.inject.Singleton

@Singleton
class AuthenticationBusinessImpl(
    private val userBusiness: UserBusiness,
    private val passwordEncoderProvider: PasswordEncoderProvider,
    private val accessTokenProvider: AccessTokenProvider
) : AuthenticationBusiness {

    override fun authenticate(email: String, rawPassword: String): User {
        val user = findUserByCredentials(email, rawPassword).apply {
            token = getAccessToken(this)
        }

        return userBusiness.update(user)
    }

    private fun findUserByCredentials(email: String, rawPassword: String): User {
        val user = userBusiness.findByEmail(email)

        if (passwordsDoesNotMatch(rawPassword, user.password)) {
            throw InvalidPasswordException()
        }

        return user
    }

    private fun getAccessToken(user: User) = accessTokenProvider.generate(user.id.toString())

    private fun passwordsDoesNotMatch(rawPassword: String, encodedPassword: String) = passwordEncoderProvider
        .matches(rawPassword, encodedPassword)
        .not()

}