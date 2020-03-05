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
import io.realworld.app.micronaut.domain.business.AuthenticationBusiness
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.web.dto.UserDto
import java.util.UUID

@MicronautTest
class UsersControllerTest(
    @Client("/api") private val httpClient: HttpClient,
    private val userBusiness: UserBusiness,
    private val authenticationBusiness: AuthenticationBusiness
) : AnnotationSpec() {

    @MockBean(UserBusiness::class)
    fun userBusiness() : UserBusiness { return mockk() }

    @MockBean(AuthenticationBusiness::class)
    fun authenticationBusiness() : AuthenticationBusiness { return mockk() }

    @Test
    fun `should create a new user when valid user data`() {
        val user = User(UUID.randomUUID(), "Almir Jr.", "almirjr.87@gmail.com", "123456")
        val userBusinessMock = getMock(userBusiness)
        every { userBusinessMock.save(any()) } returns user

        val userCreateRequest = UserDto.Request.Create("Almir Jr.", "almirjr.87@gmail.com", "123456")
        val request = HttpRequest.POST("/users", userCreateRequest)
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)

        response.status shouldBe HttpStatus.CREATED
        response.header(HttpHeaders.LOCATION) shouldBe "/api/users/${user.id}"
        response.body.get().username shouldBe "Almir Jr."
        response.body.get().email shouldBe "almirjr.87@gmail.com"
    }

    @Test
    fun `should return user data when valid user credentials`() {
        val user = User(
            UUID.randomUUID(),
            "Almir Jr.",
            "almirjr.87@gmail.com",
            "123456",
            "123.456.789"
        )
        val authenticationBusinessMock = getMock(authenticationBusiness)
        every { authenticationBusinessMock.authenticate(any(), any()) } returns user

        val userRequestLogin = UserDto.Request.Login("almirjr.87@gmail.com", "123456")
        val request = HttpRequest.POST("/users/login", userRequestLogin)
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)

        response.status shouldBe HttpStatus.OK
        response.body.get().username shouldBe "Almir Jr."
        response.body.get().email shouldBe "almirjr.87@gmail.com"
        response.body.get().token shouldBe "123.456.789"
    }

}