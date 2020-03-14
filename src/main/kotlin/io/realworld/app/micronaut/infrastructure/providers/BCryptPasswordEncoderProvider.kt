package io.realworld.app.micronaut.infrastructure.providers

import io.realworld.app.micronaut.application.providers.PasswordEncoderProvider
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.inject.Singleton

@Singleton
class BCryptPasswordEncoderProvider : PasswordEncoderProvider {

    private val delegate = BCryptPasswordEncoder()

    override fun encode(rawPassword: String): String {
        return delegate.encode(rawPassword)
    }

    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return delegate.matches(rawPassword, encodedPassword)
    }

}