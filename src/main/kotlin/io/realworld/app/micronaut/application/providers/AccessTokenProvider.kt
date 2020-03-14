package io.realworld.app.micronaut.application.providers

interface AccessTokenProvider {

    fun generate(subject: String) : String

}