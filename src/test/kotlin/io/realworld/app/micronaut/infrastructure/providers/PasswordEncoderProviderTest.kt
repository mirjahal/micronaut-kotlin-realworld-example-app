package io.realworld.app.micronaut.infrastructure.providers

import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.test.annotation.MicronautTest
import io.realworld.app.micronaut.application.providers.PasswordEncoderProvider

@MicronautTest
class PasswordEncoderProviderTest(
    private val passwordEncoderProvider : PasswordEncoderProvider
) : AnnotationSpec() {

    @Test
    fun `should check password encoded is not equals raw password when encode`() {
        val encodedPassword = passwordEncoderProvider.encode("123456")

        encodedPassword shouldNotBe "123456"
    }

    @Test
    fun `should be true when passwords match`() {
        val rawPassword = "123456"
        val encodedPassword = passwordEncoderProvider.encode(rawPassword)

        passwordEncoderProvider.matches(rawPassword, encodedPassword).shouldBeTrue()
    }

    @Test
    fun `should be false when passwords does not match`() {
        val rawPassword = "123456"
        val encodedPassword = passwordEncoderProvider.encode(rawPassword)

        passwordEncoderProvider.matches("myPassword", encodedPassword).shouldBeFalse()
    }

}