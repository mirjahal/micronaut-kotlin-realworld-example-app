package io.realworld.app.micronaut.web.dto

import com.fasterxml.jackson.annotation.JsonRootName
import io.micronaut.core.annotation.Introspected
import io.realworld.app.micronaut.domain.entity.User
import javax.validation.constraints.NotBlank

@JsonRootName("user")
sealed class UserDto {

    sealed class Request {

        @Introspected
        data class Create(
            @get:NotBlank val username: String,
            @get:NotBlank val email: String,
            @get:NotBlank val password: String
        ) : UserDto() {
            companion object {
                fun toDomain(userDtoRequestCreate: Create) : User {
                    return User(
                        username = userDtoRequestCreate.username,
                        email = userDtoRequestCreate.email,
                        password = userDtoRequestCreate.password
                    )
                }
            }
        }

        data class Login(
            @get:NotBlank val email: String,
            @get:NotBlank val password: String
        ) : UserDto()

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