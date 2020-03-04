package io.realworld.app.micronaut

import io.kotlintest.matchers.string.shouldNotBeBlank
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import io.realworld.app.micronaut.web.dto.UserDto

@MicronautTest
class IntegrationTest(
    @Client("/api") private val httpClient: HttpClient
) : AnnotationSpec() {

    @Test
    fun `should return user data when create a new user with valid data`() {
        val userCreateRequest = UserDto.Request.Create("Almir Jr.", "almirjr.87@gmail.com", "123456")
        val request = HttpRequest.POST("/users", userCreateRequest)
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)

        response.status shouldBe HttpStatus.CREATED
        response.body.get().username shouldBe "Almir Jr."
        response.body.get().email shouldBe "almirjr.87@gmail.com"
        response.body.get().token.shouldBeNull()
    }

    @Test
    fun `should return user data when success in login`() {
        val userLoginRequest = UserDto.Request.Login("almirjr.87@gmail.com", "123456")
        val request = HttpRequest.POST("/users/login", userLoginRequest)
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)

        response.status shouldBe HttpStatus.OK
        response.body.get().username shouldBe "Almir Jr."
        response.body.get().email shouldBe "almirjr.87@gmail.com"
        response.body.get().token.shouldNotBeBlank()
    }

}