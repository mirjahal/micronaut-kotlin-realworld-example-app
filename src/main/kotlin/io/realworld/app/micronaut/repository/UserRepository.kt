package io.realworld.app.micronaut.repository

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.realworld.app.micronaut.domain.entity.User
import java.util.Optional
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, UUID> {

    fun findByEmail(email: String) : Optional<User>
    fun findByUsername(username: String): Optional<User>

}