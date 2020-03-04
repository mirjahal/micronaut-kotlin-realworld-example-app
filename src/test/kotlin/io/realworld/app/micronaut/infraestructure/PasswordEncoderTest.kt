package io.realworld.app.micronaut.infraestructure

import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.security.authentication.providers.PasswordEncoder
import io.micronaut.test.annotation.MicronautTest

@MicronautTest
class PasswordEncoderTest(
    private val passwordEncoder : PasswordEncoder
) : AnnotationSpec() {

    @Test
    fun `should check password encoded is not equals raw password when encode`() {
        val encodedPassword = passwordEncoder.encode("123456")

        encodedPassword shouldNotBe "123456"
    }

    @Test
    fun `should be true when passwords match`() {
        val rawPassword = "123456"
        val encodedPassword = passwordEncoder.encode(rawPassword)

        passwordEncoder.matches(rawPassword, encodedPassword).shouldBeTrue()
    }

    @Test
    fun `should be false when passwords does not match`() {
        val rawPassword = "123456"
        val encodedPassword = passwordEncoder.encode(rawPassword)

        passwordEncoder.matches("myPassword", encodedPassword).shouldBeFalse()
    }

}