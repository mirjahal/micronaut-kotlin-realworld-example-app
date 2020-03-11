package io.realworld.app.micronaut.web.controller

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.context.annotation.Value
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

@MicronautTest
class UsersControllerTest(
    @Client("/api") private val httpClient: HttpClient,
    @Value("\${realworld.token}") private val token: String,
    private val authenticationBusiness: AuthenticationBusiness,
    private val userBusiness: UserBusiness
) : AnnotationSpec() {

    @MockBean(UserBusiness::class)
    fun userBusiness() : UserBusiness { return mockk() }

    @MockBean(AuthenticationBusiness::class)
    fun authenticationBusiness() : AuthenticationBusiness { return mockk() }

    @Test
    fun `should create a new user when valid user data`() {
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = "123456")
        val userBusinessMock = getMock(userBusiness)

        every { userBusinessMock.save(any()) } returns user

        val data = UserDto.Request.Create(user.username, user.email, user.password)
        val request = HttpRequest.POST("/users", data)
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)
        val body = response.body.get()

        response.status shouldBe HttpStatus.CREATED
        response.header(HttpHeaders.LOCATION) shouldBe "/api/users/${user.id}"
        body.username shouldBe user.username
        body.email shouldBe user.email
    }

    @Test
    fun `should return user data when valid user credentials`() {
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = "123456", token = token)
        val authenticationBusinessMock = getMock(authenticationBusiness)

        every { authenticationBusinessMock.authenticate(any(), any()) } returns user

        val data = UserDto.Request.Login(user.email, user.password)
        val request = HttpRequest.POST("/users/login", data)
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)
        val body = response.body.get()

        response.status shouldBe HttpStatus.OK
        body.username shouldBe user.username
        body.email shouldBe user.email
        body.token shouldBe user.token
    }

}