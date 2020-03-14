package io.realworld.app.micronaut.infrastructure.repository

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.realworld.app.micronaut.domain.entity.UserFollow
import io.realworld.app.micronaut.domain.entity.UserFollowPK
import io.realworld.app.micronaut.domain.repository.UserFollowRepository

@Repository
interface UserFollowJpaRepository : JpaRepository<UserFollow, UserFollowPK>, UserFollowRepository