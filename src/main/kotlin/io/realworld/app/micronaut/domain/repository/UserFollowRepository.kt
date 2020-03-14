package io.realworld.app.micronaut.domain.repository

import io.realworld.app.micronaut.domain.entity.UserFollow
import io.realworld.app.micronaut.domain.entity.UserFollowPK
import java.util.Optional

interface UserFollowRepository {

    fun findById(userFollowPK: UserFollowPK): Optional<UserFollow>
    fun save(userFollow: UserFollow): UserFollow
    fun delete(userFollow: UserFollow)

}