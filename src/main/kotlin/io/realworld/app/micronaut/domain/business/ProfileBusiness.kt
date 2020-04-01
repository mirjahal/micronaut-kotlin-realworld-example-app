package io.realworld.app.micronaut.domain.business

import io.realworld.app.micronaut.application.data.ProfileData
import io.realworld.app.micronaut.domain.entity.User
import java.util.UUID

interface ProfileBusiness {

    fun get(username: String, currentUserId: UUID? = null): ProfileData
    fun followUser(username: String, currentUserId: UUID): ProfileData
    fun unfollowUser(username: String, currentUserId: UUID): ProfileData
    fun isFollowingUser(followedUser: User, currentUserId: UUID): Boolean

}