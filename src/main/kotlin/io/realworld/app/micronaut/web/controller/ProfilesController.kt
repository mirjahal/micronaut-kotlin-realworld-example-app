package io.realworld.app.micronaut.web.controller

import io.micronaut.context.annotation.Parameter
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS
import io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED
import io.realworld.app.micronaut.domain.business.ProfileBusiness
import io.realworld.app.micronaut.web.dto.ProfileDto
import java.net.URI
import java.security.Principal
import java.util.UUID

@Controller("/profiles")
class ProfilesController(
    private val profileBusiness : ProfileBusiness
) {

    @Get("/{username}")
    @Secured(IS_AUTHENTICATED, IS_ANONYMOUS)
    fun get(@Parameter username: String, principal: Principal?) : HttpResponse<ProfileDto.Response> {
        val currentUserId = if (principal != null) getUserUuid(principal.name) else null
        val profileDto = profileBusiness.get(username, currentUserId).let { profile ->
            ProfileDto.Response.fromData(profile)
        }

        return HttpResponse.ok(profileDto)
    }

    @Post("/{username}/follow")
    @Secured(IS_AUTHENTICATED)
    fun follow(@Parameter username: String, principal: Principal): HttpResponse<ProfileDto.Response> {
        val profileDto = profileBusiness.followUser(username, getUserUuid(principal.name)).let { profile ->
            ProfileDto.Response.fromData(profile)
        }

        return HttpResponse.created(profileDto, URI.create("/api/profiles/${profileDto.username}"))
    }

    @Delete("/{username}/follow")
    @Secured(IS_AUTHENTICATED)
    fun unfollow(@Parameter username: String, principal: Principal): HttpResponse<ProfileDto.Response> {
        val profileDto = profileBusiness.unfollowUser(username, getUserUuid(principal.name)).let { profile ->
            ProfileDto.Response.fromData(profile)
        }

        return HttpResponse.ok(profileDto)
    }

    private fun getUserUuid(idAsString: String) = UUID.fromString(idAsString)

}