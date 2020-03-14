package io.realworld.app.micronaut.application.business

import io.realworld.app.micronaut.application.exception.ResourceNotFoundException
import io.realworld.app.micronaut.application.providers.PasswordEncoderProvider
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.domain.repository.UserRepository
import java.util.UUID
import javax.inject.Singleton

@Singleton
class UserBusinessImpl(
    private val userRepository: UserRepository,
    private val passwordEncoderProvider: PasswordEncoderProvider
) : UserBusiness {

    override fun save(user: User): User {
        encodeUserPassword(user)
        return userRepository.save(user)
    }

    override fun update(user: User): User {
        encodeUserPassword(user)
        return userRepository.update(user)
    }

    override fun findByEmail(email: String): User {
        return userRepository
            .findByEmail(email)
            .orElseThrow { ResourceNotFoundException() }
    }

    override fun findById(id: UUID): User {
        return userRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException() }
    }

    override fun findByUsername(username: String): User {
        return userRepository
            .findByUsername(username)
            .orElseThrow { ResourceNotFoundException() }
    }

    private fun encodeUserPassword(user: User) {
        user.password = passwordEncoderProvider.encode(user.password)
    }

}