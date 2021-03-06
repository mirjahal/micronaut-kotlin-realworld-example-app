package io.realworld.app.micronaut.infrastructure.web.dto

import com.fasterxml.jackson.annotation.JsonRootName
import io.micronaut.core.annotation.Introspected
import io.realworld.app.micronaut.domain.entity.User
import javax.validation.constraints.NotBlank

@JsonRootName("user")
sealed class UserDto {

    sealed class Request : UserDto() {
        @Introspected
        data class Create(
            @get:NotBlank val username: String,
            @get:NotBlank val email: String,
            @get:NotBlank val password: String
        ) : Request() {
            companion object {
                fun toDomain(userDto: Create) : User {
                    return User(
                        username = userDto.username,
                        email = userDto.email,
                        password = userDto.password
                    )
                }
            }
        }

        @Introspected
        data class Update(
            val username: String? = null,
            val email: String? = null,
            val password: String? = null,
            val image: String? = null,
            val bio: String? = null
        ) : Request()

        data class Login(
            @get:NotBlank val email: String,
            @get:NotBlank val password: String
        ) : Request()
    }

    class Response(
        val username: String,
        val email: String,
        val token: String? = null,
        val bio: String? = null,
        val image: String? = null
    ) : UserDto() {
        companion object {
            fun fromEntity(user: User) : Response {
                return Response(
                    username = user.username,
                    email = user.email,
                    token = user.token,
                    bio = user.bio,
                    image = user.image
                )
            }
        }
    }

}