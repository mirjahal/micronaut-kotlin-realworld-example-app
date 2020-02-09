package io.realworld.app.micronaut.web.controller

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotlintest.MicronautKotlinTestExtension.getMock
import io.mockk.every
import io.mockk.mockk
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.web.dto.UserDto
import io.realworld.app.micronaut.web.handler.Errors
import java.util.UUID

@MicronautTest
class UserControllerTest(
    @Client("/api") private val httpClient: HttpClient,
    private val userBusiness: UserBusiness
) : AnnotationSpec() {

    @MockBean(UserBusiness::class)
    fun userBusiness() : UserBusiness { return mockk() }

    @Test
    fun `given valid user data when execute request then create a new user`() {
        val user = User(UUID.randomUUID(), "Almir Jr.", "almirjr.87@gmail.com", "123456")
        val userBusinessMock = getMock(userBusiness)
        every { userBusinessMock.save(any()) } returns user

        val userRequest = UserDto.Request("Almir Jr.", "almirjr.87@gmail.com", "123456")
        val request = HttpRequest.POST("/users", userRequest)
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)

        response.status shouldBe HttpStatus.CREATED
        response.header(HttpHeaders.LOCATION) shouldBe "/api/users/${user.id}"
        response.body.get().username shouldBe "Almir Jr."
        response.body.get().email shouldBe "almirjr.87@gmail.com"
    }

    @Test
    fun `given valid user credentials when execute login request with success then return user data`() {

    }

}