package io.realworld.app.micronaut.domain.business

import io.realworld.app.micronaut.application.data.ProfileData
import java.util.UUID

interface ProfileBusiness {

    fun get(username: String, currentUserId: UUID? = null): ProfileData
    fun followUser(username: String, currentUserId: UUID): ProfileData
    fun unfollowUser(username: String, currentUserId: UUID): ProfileData

}