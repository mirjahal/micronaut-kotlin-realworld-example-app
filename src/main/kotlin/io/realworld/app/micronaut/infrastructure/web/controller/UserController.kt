package io.realworld.app.micronaut.infrastructure.web.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.infrastructure.extensions.toUUID
import io.realworld.app.micronaut.infrastructure.web.dto.UserDto
import java.security.Principal
import java.util.UUID

@Controller("/user")
class UserController(
    private var userBusiness: UserBusiness
) {

    @Get
    @Secured(IS_AUTHENTICATED)
    fun getCurrent(principal: Principal) : HttpResponse<UserDto.Response> {
        val user = userBusiness.findById(principal.name.toUUID())
        val userResponse = UserDto.Response.fromEntity(user)

        return HttpResponse.ok(userResponse)
    }

    @Put
    @Secured(IS_AUTHENTICATED)
    fun update(@Body userDto: UserDto.Request.Update, principal: Principal) : HttpResponse<UserDto.Response> {
        val user = userBusiness.findById(principal.name.toUUID()).let { user ->
            user.copy(
                username = userDto.username ?: user.username,
                email = userDto.email ?: user.email,
                password = userDto.password ?: user.password,
                image = userDto.image ?: user.image,
                bio = userDto.bio ?: user.bio
            )
        }

        userBusiness.update(user)

        return HttpResponse.ok(UserDto.Response.fromEntity(user))
    }

}