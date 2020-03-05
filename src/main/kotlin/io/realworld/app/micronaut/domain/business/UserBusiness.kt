package io.realworld.app.micronaut.domain.business

import io.realworld.app.micronaut.domain.entity.User

interface UserBusiness {

    fun save(user: User) : User
    fun findByEmail(email: String): User

}