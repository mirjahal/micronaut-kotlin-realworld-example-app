package io.realworld.app.micronaut.web.controller

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.context.annotation.Value
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

@MicronautTest
class UserControllerTest(
    @Client("/api") private val httpClient: HttpClient,
    @Value("\${realworld.token}") private val token: String,
    private val userBusiness: UserBusiness
) : AnnotationSpec() {

    @MockBean(UserBusiness::class)
    fun userBusiness() : UserBusiness { return mockk() }

    @Test
    fun `should return current user when request by token`() {
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = "123456", token = token)
        val userBusinessMock = getMock(userBusiness)

        every { userBusinessMock.findById(any()) } returns user

        val request = HttpRequest.GET<UserDto.Response>("/user").apply {
            header(HttpHeaders.AUTHORIZATION, "$AUTHORIZATION_PREFIX_BEARER $token")
        }
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)
        val body = response.body.get()

        response.status shouldBe HttpStatus.OK
        body.username shouldBe user.username
        body.email shouldBe user.email
        body.token shouldBe user.token
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
        val updatedUser = user.copy(image = "image.jpg", bio = "Short bio")
        val userBusinessMock = getMock(userBusiness)

        every { userBusinessMock.findById(any()) } returns user
        every { userBusinessMock.update(any()) } returns updatedUser

        val data = UserDto.Request.Update(image = updatedUser.image, bio = updatedUser.bio)
        val request = HttpRequest.PUT("/user", data).apply {
            header(HttpHeaders.AUTHORIZATION, "$AUTHORIZATION_PREFIX_BEARER $token")
        }
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)
        val body = response.body.get()

        response.status shouldBe HttpStatus.OK
        body.image shouldBe updatedUser.image
        body.bio shouldBe updatedUser.bio
    }

}