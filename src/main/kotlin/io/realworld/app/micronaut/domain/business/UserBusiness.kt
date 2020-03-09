package io.realworld.app.micronaut.domain.business

import io.realworld.app.micronaut.domain.entity.User
import java.util.UUID

interface UserBusiness {

    fun save(user: User) : User
    fun update(user: User) : User
    fun findByEmail(email: String): User
    fun findById(id: UUID): User

}