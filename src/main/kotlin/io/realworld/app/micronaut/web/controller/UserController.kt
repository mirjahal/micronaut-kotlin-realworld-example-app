package io.realworld.app.micronaut.web.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.web.dto.UserDto
import java.net.URI
import javax.validation.Valid

@Validated
@Controller("/users")
class UserController(
    private var userBusiness: UserBusiness
) {

    @Post
    fun create(@Body @Valid userRequest: UserDto.Request) : HttpResponse<UserDto.Response> {
        val user = userBusiness.save(UserDto.Request.toDomain(userRequest))
        val userResponse = UserDto.Response.fromEntity(user)

        return HttpResponse.created(userResponse, URI.create("/api/users/${user.id}"))
    }

}