package io.realworld.app.micronaut.application.providers

interface SlugProvider {

    fun slug(text: String): String

}