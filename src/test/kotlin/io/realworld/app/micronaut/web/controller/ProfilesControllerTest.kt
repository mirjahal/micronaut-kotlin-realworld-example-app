package io.realworld.app.micronaut.web.controller

import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.http.HttpHeaderValues
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
import io.realworld.app.micronaut.domain.business.ProfileBusiness
import io.realworld.app.micronaut.domain.data.Profile
import io.realworld.app.micronaut.web.dto.ProfileDto

@MicronautTest
class ProfilesControllerTest(
    @Client("/api") private val httpClient: HttpClient,
    private val profileBusiness : ProfileBusiness
) : AnnotationSpec() {

    @MockBean(ProfileBusiness::class)
    fun profileBusiness() : ProfileBusiness { return mockk() }

    private val token = "eyJhbGciOiJIUzI1NiJ9" +
        ".eyJzdWIiOiJhMTY2ZDBmNy01YzRhLTRjMDItYTc4ZC0wYjM5MmFiZWQ0ZjAiLCJuYmYiOjE1ODM1OTI5MjAsInJvbGVzIjpbXSwiaXNzIjoibWljcm9uYXV0IiwiaWF0IjoxNTgzNTkyOTIwfQ" +
        ".o13dMnOsB9yAbY6YaU6Yceq38BEmr1EAwk2UjqINgpM"

    @Test
    fun `should return a profile for user authenticated when valid username as path parameter`() {
        val username = "mirjahal"
        val profileBusinessMock = getMock(profileBusiness)
        every { profileBusinessMock.get(any(), any()) } returns Profile(username, "Mini bio", "image.jpg", false)

        val request = HttpRequest.GET<ProfileDto.Response>("/profiles/$username").apply {
            header(HttpHeaders.AUTHORIZATION, "${HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER} $token")
        }
        val response = httpClient.toBlocking().exchange(request, ProfileDto.Response::class.java)

        response.status shouldBe HttpStatus.OK
        response.body.get().username shouldBe username
        response.body.get().bio shouldBe "Mini bio"
        response.body.get().image shouldBe "image.jpg"
        response.body.get().following!!.shouldBeFalse()
    }

    @Test
    fun `should return a profile for anonymous user when valid username as path parameter`() {
        val username = "mirjahal"
        val profileBusinessMock = getMock(profileBusiness)
        every { profileBusinessMock.get(any(), any()) } returns Profile(username, "Mini bio", "image.jpg")

        val request = HttpRequest.GET<ProfileDto.Response>("/profiles/$username")
        val response = httpClient.toBlocking().exchange(request, ProfileDto.Response::class.java)

        response.status shouldBe HttpStatus.OK
        response.body.get().username shouldBe username
        response.body.get().bio shouldBe "Mini bio"
        response.body.get().image shouldBe "image.jpg"
        response.body.get().following shouldBe null
    }

}