package io.realworld.app.micronaut.application.providers

interface PasswordEncoderProvider {

    fun encode(rawPassword: String): String
    fun matches(rawPassword: String, encodedPassword: String): Boolean

}