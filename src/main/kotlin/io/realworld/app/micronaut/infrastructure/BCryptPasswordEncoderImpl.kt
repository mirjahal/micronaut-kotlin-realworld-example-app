package io.realworld.app.micronaut.infrastructure

import io.micronaut.security.authentication.providers.PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.inject.Singleton

@Singleton
class BCryptPasswordEncoderImpl : PasswordEncoder {

    private val delegate = BCryptPasswordEncoder()

    override fun encode(rawPassword: String): String {
        return delegate.encode(rawPassword)
    }

    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return delegate.matches(rawPassword, encodedPassword)
    }

}