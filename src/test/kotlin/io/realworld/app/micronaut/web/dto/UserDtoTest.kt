package io.realworld.app.micronaut.web.dto

import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.context.annotation.Value
import io.micronaut.test.annotation.MicronautTest
import io.realworld.app.micronaut.domain.entity.User
import java.util.UUID

@MicronautTest
class UserDtoTest(
    @Value("\${realworld.token}") private val token: String
) : AnnotationSpec() {

    @Test
    fun `should return a user from RequestCreate when execute toDomain method`() {
        val userDto = UserDto.Request.Create("Almir Jr.", "almirjr.87@gmail.com", "123456")

        val user = UserDto.Request.Create.toDomain(userDto)

        user.username shouldBe userDto.username
        user.email shouldBe userDto.email
        user.password shouldBe userDto.password
    }

    @Test
    fun `should return a UserResponse with all properties filled when execute fromEntity method`() {
        val user = User(
            username = "Almir Jr.",
            email = "almirjr.87@gmail.com",
            password = "123456",
            token = token,
            bio = "Why is every Severino called Bio?",
            image = "photo.png"
        )

        val userResponse = UserDto.Response.fromEntity(user)

        userResponse.username shouldBe user.username
        userResponse.email shouldBe user.email
        userResponse.token shouldBe user.token
        userResponse.bio shouldBe user.bio
        userResponse.image shouldBe user.image
    }

    @Test
    fun `should return a UserResponse when execute fromEntity method`() {
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = "123456")

        val userResponse = UserDto.Response.fromEntity(user)

        userResponse.username shouldBe user.username
        userResponse.email shouldBe user.email
        userResponse.token.shouldBeNull()
        userResponse.bio.shouldBeNull()
        userResponse.image.shouldBeNull()
    }

}