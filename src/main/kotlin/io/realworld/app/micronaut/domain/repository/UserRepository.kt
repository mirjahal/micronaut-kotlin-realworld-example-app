package io.realworld.app.micronaut.domain.repository

import io.realworld.app.micronaut.domain.entity.User
import java.util.Optional
import java.util.UUID

interface UserRepository {

    fun findById(id: UUID): Optional<User>
    fun findByEmail(email: String) : Optional<User>
    fun findByUsername(username: String): Optional<User>
    fun update(user: User): User
    fun save(user: User): User

}