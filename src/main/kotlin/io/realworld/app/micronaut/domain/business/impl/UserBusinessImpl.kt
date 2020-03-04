package io.realworld.app.micronaut.domain.business.impl

import io.micronaut.security.authentication.providers.PasswordEncoder
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.repository.UserRepository
import javax.inject.Singleton

@Singleton
class UserBusinessImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserBusiness {

    override fun save(user: User): User {
        user.password = passwordEncoder.encode(user.password)
        return userRepository.save(user)
    }

}