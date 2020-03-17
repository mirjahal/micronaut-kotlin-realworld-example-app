package io.realworld.app.micronaut.domain.repository

import io.realworld.app.micronaut.domain.entity.UserFollow
import io.realworld.app.micronaut.domain.entity.UserFollowKey

interface UserFollowRepository : GenericEntityRepository<UserFollow, UserFollowKey>