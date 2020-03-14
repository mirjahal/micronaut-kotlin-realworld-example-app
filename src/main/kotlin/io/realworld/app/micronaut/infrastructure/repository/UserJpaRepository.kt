package io.realworld.app.micronaut.infrastructure.repository

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.domain.repository.UserRepository
import java.util.UUID

@Repository
interface UserJpaRepository : JpaRepository<User, UUID>, UserRepository