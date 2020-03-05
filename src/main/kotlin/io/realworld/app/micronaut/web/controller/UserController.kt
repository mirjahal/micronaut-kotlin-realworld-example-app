package io.realworld.app.micronaut.web.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.web.dto.UserDto
import java.security.Principal

@Controller("/user")
class UserController(
    private var userBusiness: UserBusiness
) {

    @Get
    @Secured(IS_AUTHENTICATED)
    fun getCurrentUser(principal: Principal) : HttpResponse<UserDto.Response> {
        val user = userBusiness.findByEmail(principal.name)
        val userResponse = UserDto.Response.fromEntity(user)

        return HttpResponse.ok(userResponse)
    }

}