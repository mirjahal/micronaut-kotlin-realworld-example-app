package io.realworld.app.micronaut.application.data

data class Profile(
    val username: String,
    val bio: String? = null,
    val image: String? = null,
    var following: Boolean? = null
)