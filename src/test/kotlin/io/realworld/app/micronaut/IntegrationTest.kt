package io.realworld.app.micronaut

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
    fun `given valid user data when execute request with data then create a new user`() {
        val userRequest = UserDto.Request("Almir Jr.", "almirjr.87@gmail.com", "123456")
        val request = HttpRequest.POST("/users", userRequest)
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)

        response.status shouldBe HttpStatus.CREATED
        response.body.get().username shouldBe "Almir Jr."
        response.body.get().email shouldBe "almirjr.87@gmail.com"
    }

}