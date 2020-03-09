package io.realworld.app.micronaut.web.controller

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.http.HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER
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
import java.util.UUID

@MicronautTest
class UserControllerTest(
    @Client("/api") private val httpClient: HttpClient,
    private val userBusiness: UserBusiness
) : AnnotationSpec() {

    @MockBean(UserBusiness::class)
    fun userBusiness() : UserBusiness { return mockk() }

    private val token = "eyJhbGciOiJIUzI1NiJ9" +
        ".eyJzdWIiOiJhMTY2ZDBmNy01YzRhLTRjMDItYTc4ZC0wYjM5MmFiZWQ0ZjAiLCJuYmYiOjE1ODM1OTI5MjAsInJvbGVzIjpbXSwiaXNzIjoibWljcm9uYXV0IiwiaWF0IjoxNTgzNTkyOTIwfQ" +
        ".o13dMnOsB9yAbY6YaU6Yceq38BEmr1EAwk2UjqINgpM"

    @Test
    fun `should return current user when request by token`() {
        val user = User(
            UUID.randomUUID(),
            "Almir Jr.",
            "almirjr.87@gmail.com",
            "123456",
            token
        )
        val userBusinessMock = getMock(userBusiness)
        every { userBusinessMock.findById(any()) } returns user

        val request = HttpRequest.GET<UserDto.Response>("/user").apply {
            header(HttpHeaders.AUTHORIZATION, "$AUTHORIZATION_PREFIX_BEARER ${user.token}")
        }
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)

        response.status shouldBe HttpStatus.OK
        response.body.get().username shouldBe user.username
        response.body.get().email shouldBe user.email
        response.body.get().token shouldBe user.token
    }

    @Test
    fun `should update a user when request it`() {
        val user = User(
            email = "almirjr.87@gmail.com",
            username = "Almir Jr.",
            password = "abcdef",
            image = "https://avatars3.githubusercontent.com/u/3357439?v=4",
            bio = "Filho, Marido e Pai",
            token = token
        )
        val userBusinessMock = getMock(userBusiness)
        every { userBusinessMock.findById(any()) } returns user
        every { userBusinessMock.update(any()) } returns user.copy(image = "image.jpg", bio = "Short bio")

        val userUpdateRequest = UserDto.Request.Update(image = "image.jpg", bio = "Short bio")
        val request = HttpRequest.PUT("/user", userUpdateRequest).apply {
            header(HttpHeaders.AUTHORIZATION, "$AUTHORIZATION_PREFIX_BEARER $token")
        }
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)

        response.status shouldBe HttpStatus.OK
        response.body.get().image shouldBe userUpdateRequest.image
        response.body.get().bio shouldBe userUpdateRequest.bio
    }

}