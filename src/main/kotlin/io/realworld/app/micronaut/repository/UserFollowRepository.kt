package io.realworld.app.micronaut.repository

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.realworld.app.micronaut.domain.entity.UserFollow
import io.realworld.app.micronaut.domain.entity.UserFollowPK

@Repository
interface UserFollowRepository : JpaRepository<UserFollow, UserFollowPK> {

}