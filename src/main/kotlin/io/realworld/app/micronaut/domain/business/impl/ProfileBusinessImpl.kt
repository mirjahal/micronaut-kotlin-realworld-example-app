package io.realworld.app.micronaut.domain.business.impl

import io.realworld.app.micronaut.domain.business.ProfileBusiness
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.data.Profile
import io.realworld.app.micronaut.domain.entity.UserFollow
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
            profile.following = userFollowRepository.findById(UserFollowPK(followerUser, followedUser)).isPresent
        }

        return profile
    }

    override fun followUserByUsername(username: String, currentUserId: UUID): Profile {
        val followerUser = userBusiness.findById(currentUserId)
        val followedUser = userBusiness.findByUsername(username)

        val userFollowPK = UserFollowPK(followerUser, followedUser)
        val userFollow = UserFollow(userFollowPK)
        userFollowRepository.save(userFollow)

        return Profile(followedUser.username, followedUser.bio, followedUser.image, true)
    }

}