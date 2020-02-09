package io.realworld.app.micronaut.domain.business.impl

import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.repository.UserRepository
import javax.inject.Singleton

@Singleton
class UserBusinessImpl(
    private val userRepository: UserRepository
) : UserBusiness {

    override fun save(user: User): User {
        return userRepository.save(user)
    }

}