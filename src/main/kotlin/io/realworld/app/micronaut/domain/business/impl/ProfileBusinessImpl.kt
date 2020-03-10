package io.realworld.app.micronaut.domain.business.impl

import io.realworld.app.micronaut.domain.business.ProfileBusiness
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.data.Profile
import io.realworld.app.micronaut.domain.entity.UserFollowPK
import io.realworld.app.micronaut.repository.UserFollowRepository
import java.util.UUID
import javax.inject.Singleton

@Singleton
class ProfileBusinessImpl(
    private val userBusiness: UserBusiness,
    private val userFollowRepository: UserFollowRepository
) : ProfileBusiness {

    override fun get(username: String, currentUserId: UUID?): Profile {
        val followedUser = userBusiness.findByUsername(username)
        val profile = Profile(followedUser.username, followedUser.bio, followedUser.image)

        if (currentUserId != null) {
            val followerUser = userBusiness.findById(currentUserId)
            profile.following = userFollowRepository.existsById(UserFollowPK(followerUser, followedUser))
        }

        return profile
    }

}