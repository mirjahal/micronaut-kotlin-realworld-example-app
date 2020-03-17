package io.realworld.app.micronaut.infrastructure.repository

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.GenericRepository
import io.realworld.app.micronaut.domain.entity.UserFollow
import io.realworld.app.micronaut.domain.entity.UserFollowPK
import io.realworld.app.micronaut.domain.repository.UserFollowRepository

@Repository
interface UserFollowJpaRepository : GenericRepository<UserFollow, UserFollowPK>, UserFollowRepository