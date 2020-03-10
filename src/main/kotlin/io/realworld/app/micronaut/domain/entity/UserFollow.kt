package io.realworld.app.micronaut.domain.entity

import javax.persistence.*

@Entity
@Table(name = "user_follow")
data class UserFollow(
    @EmbeddedId
    val userFollowPK: UserFollowPK
)