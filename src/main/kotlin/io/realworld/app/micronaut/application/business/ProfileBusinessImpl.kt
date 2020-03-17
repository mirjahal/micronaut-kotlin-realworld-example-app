package io.realworld.app.micronaut.application.business

import io.realworld.app.micronaut.application.data.ProfileData
import io.realworld.app.micronaut.domain.business.ProfileBusiness
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.domain.entity.UserFollow
import io.realworld.app.micronaut.domain.entity.UserFollowPK
import io.realworld.app.micronaut.domain.repository.UserFollowRepository
import java.util.UUID
import javax.inject.Singleton

@Singleton
class ProfileBusinessImpl(
    private val userBusiness: UserBusiness,
    private val userFollowRepository: UserFollowRepository
) : ProfileBusiness {

    override fun get(username: String, currentUserId: UUID?): ProfileData {
        val followedUser = userBusiness.findByUsername(username)
        val profile = buildProfile(followedUser)

        if (currentUserId != null) {
            val followerUser = userBusiness.findById(currentUserId)
            profile.following = userFollowRepository.findById(UserFollowPK(followerUser, followedUser)).isPresent
        }

        return profile
    }

    override fun followUser(username: String, currentUserId: UUID): ProfileData {
        val userFollow = userFollowRepository.save(getUserFollow(username, currentUserId))

        return buildProfile(userFollow.userFollowPK.followedUser, true)
    }

    override fun unfollowUser(username: String, currentUserId: UUID): ProfileData {
        val userFollow = getUserFollow(username, currentUserId)
        userFollowRepository.delete(userFollow)

        return buildProfile(userFollow.userFollowPK.followedUser, false)
    }

    private fun getUserFollow(username: String, currentUserId: UUID): UserFollow {
        val followerUser = userBusiness.findById(currentUserId)
        val followedUser = userBusiness.findByUsername(username)
        val userFollowPK = UserFollowPK(followerUser, followedUser)

        return UserFollow(userFollowPK)
    }

    private fun buildProfile(user: User, following: Boolean? = null) = ProfileData(user.username, user.bio, user.image, following)

}