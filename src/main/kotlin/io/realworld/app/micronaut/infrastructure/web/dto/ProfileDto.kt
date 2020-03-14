package io.realworld.app.micronaut.infrastructure.web.dto

import com.fasterxml.jackson.annotation.JsonRootName
import io.realworld.app.micronaut.application.data.Profile

@JsonRootName("profile")
sealed class ProfileDto {

    data class Response(
        val username: String,
        val bio: String? = null,
        val image: String? = null,
        val following: Boolean? = null
    ) : ProfileDto() {
        companion object {
            fun fromData(profile: Profile) : Response {
                return Response(
                    profile.username,
                    profile.bio,
                    profile.image,
                    profile.following
                )
            }
        }
    }

}