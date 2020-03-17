package io.realworld.app.micronaut.infrastructure.providers

import com.github.slugify.Slugify
import io.realworld.app.micronaut.application.providers.SlugProvider
import javax.inject.Singleton

@Singleton
class SlugifyProvider : SlugProvider {

    override fun slug(text: String) = Slugify().slugify(text)

}