package io.realworld.app.micronaut.web.controller

import io.micronaut.context.annotation.Parameter
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS
import io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED
import io.realworld.app.micronaut.domain.business.ProfileBusiness
import io.realworld.app.micronaut.web.dto.ProfileDto
import java.security.Principal
import java.util.UUID

@Controller("/profiles")
class ProfilesController(
    private val profileBusiness : ProfileBusiness
) {

    @Get("/{username}")
    @Secured(IS_AUTHENTICATED, IS_ANONYMOUS)
    fun get(@Parameter username: String, principal: Principal?) : HttpResponse<ProfileDto.Response> {
        val currentUserId = if (principal != null) UUID.fromString(principal.name) else null
        val profileDto = profileBusiness.get(username, currentUserId).let { profile ->
            ProfileDto.Response.fromData(profile)
        }

        return HttpResponse.ok(profileDto)
    }

}