package io.realworld.app.micronaut.web.dto

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.test.annotation.MicronautTest
import io.realworld.app.micronaut.domain.entity.User
import java.util.UUID

@MicronautTest
class UserDtoTest : AnnotationSpec() {

    @Test
    fun `given a valid UserRequest when execute toDomain method then return a User`() {
        val userRequest = UserDto.Request("Almir Jr.", "almirjr.87@gmail.com", "123456")
        val user = UserDto.Request.toDomain(userRequest)

        user.username shouldBe userRequest.username
        user.email shouldBe userRequest.email
        user.password shouldBe userRequest.password
    }

    @Test
    fun `given a valid User when execute fromEntity method then return a UserResponse with all properties filled`() {
        val user = User(UUID.randomUUID(),
            "Almir Jr.",
            "almirjr.87@gmail.com",
            "123456",
            "123.456.789",
            "Why is every Severino called Bio?",
            "photo.png"
        )

        val userResponse = UserDto.Response.fromEntity(user)

        userResponse.username shouldBe user.username
        userResponse.email shouldBe user.email
        userResponse.token shouldBe user.token
        userResponse.bio shouldBe user.bio
        userResponse.image shouldBe user.image
    }

    @Test
    fun `given a valid User when execute fromEntity method then return a UserResponse`() {
        val user = User(UUID.randomUUID(),
            "Almir Jr.",
            "almirjr.87@gmail.com",
            "123456"
        )

        val userResponse = UserDto.Response.fromEntity(user)

        userResponse.username shouldBe user.username
        userResponse.email shouldBe user.email
        userResponse.token shouldBe null
        userResponse.bio shouldBe null
        userResponse.image shouldBe null
    }

}