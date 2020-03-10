package io.realworld.app.micronaut.domain.entity

import java.io.Serializable
import javax.persistence.Embeddable
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Embeddable
data class UserFollowPK(
    @ManyToOne
    @JoinColumn(name = "follower_user_id")
    val followerUser: User,

    @ManyToOne
    @JoinColumn(name = "followed_user_id")
    val followedUser: User
) : Serializable
