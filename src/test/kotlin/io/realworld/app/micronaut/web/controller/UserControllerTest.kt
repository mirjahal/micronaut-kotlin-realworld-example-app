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
import java.util.UUID

@MicronautTest
class UserControllerTest(
    @Client("/api") private val httpClient: HttpClient,
    private val userBusiness: UserBusiness
) : AnnotationSpec() {

    @MockBean(UserBusiness::class)
    fun userBusiness() : UserBusiness { return mockk() }

    @Test
    fun `should return current user when request by token`() {
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.6cD3MnZmX2xyEAWyh-GgGD11TX8SmvmHVLknuAIJ8yE"
        val user = User(
            UUID.randomUUID(),
            "Almir Jr.",
            "almirjr.87@gmail.com",
            "123456",
            token
        )
        val userBusinessMock = getMock(userBusiness)
        every { userBusinessMock.findByEmail(any()) } returns user

        val request = HttpRequest.GET<UserDto.Response>("/user").apply {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
        val response = httpClient.toBlocking().exchange(request, UserDto.Response::class.java)

        response.status shouldBe HttpStatus.OK
        response.body.get().username shouldBe "Almir Jr."
        response.body.get().email shouldBe "almirjr.87@gmail.com"
        response.body.get().token shouldBe token
    }

}