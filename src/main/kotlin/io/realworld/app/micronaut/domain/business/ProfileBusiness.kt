package io.realworld.app.micronaut.domain.business

import io.realworld.app.micronaut.domain.data.Profile
import java.util.UUID

interface ProfileBusiness {

    fun get(username: String, currentUserId: UUID? = null): Profile
    fun followUser(username: String, currentUserId: UUID): Profile
    fun unfollowUser(username: String, currentUserId: UUID): Profile

}