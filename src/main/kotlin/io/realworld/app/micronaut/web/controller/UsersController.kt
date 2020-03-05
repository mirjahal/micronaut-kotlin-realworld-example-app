package io.realworld.app.micronaut.web.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS
import io.micronaut.validation.Validated
import io.realworld.app.micronaut.domain.business.AuthenticationBusiness
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.web.dto.UserDto
import java.net.URI
import javax.validation.Valid

@Validated
@Controller("/users")
class UsersController(
    private var userBusiness: UserBusiness,
    private var authenticationBusiness: AuthenticationBusiness
) {

    @Post
    @Secured(IS_ANONYMOUS)
    fun create(@Body @Valid userCreateRequest: UserDto.Request.Create) : HttpResponse<UserDto.Response> {
        val user = userBusiness.save(
            UserDto.Request.Create.toDomain(userCreateRequest)
        )

        val userResponse = UserDto.Response.fromEntity(user)

        return HttpResponse.created(userResponse, URI.create("/api/users/${user.id}"))
    }

    @Post("/login")
    @Secured(IS_ANONYMOUS)
    fun login(@Body @Valid userLoginRequest: UserDto.Request.Login) : HttpResponse<UserDto.Response> {
        val user = authenticationBusiness.authenticate(userLoginRequest.email, userLoginRequest.password)
        val userResponse = UserDto.Response.fromEntity(user)

        return HttpResponse.ok(userResponse)
    }

}