package io.realworld.app.micronaut.domain.entity

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "user_follow")
data class UserFollow(
    @EmbeddedId
    val userFollowKey: UserFollowKey
)