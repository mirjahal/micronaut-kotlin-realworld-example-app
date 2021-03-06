package io.realworld.app.micronaut.infrastructure.repository

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.GenericRepository
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.domain.repository.UserRepository
import java.util.UUID

@Repository
interface UserJpaRepository : GenericRepository<User, UUID>, UserRepository