package io.realworld.app.micronaut.infrastructure.web.controller

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.context.annotation.Value
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
import io.realworld.app.micronaut.application.data.ProfileData
import io.realworld.app.micronaut.infrastructure.web.dto.ProfileDto

@MicronautTest
class ProfilesControllerTest(
    @Client("/api") private val httpClient: HttpClient,
    @Value("\${realworld.token}") private val token: String,
    private val profileBusiness : ProfileBusiness
) : AnnotationSpec() {

    @MockBean(ProfileBusiness::class)
    fun profileBusiness() : ProfileBusiness { return mockk() }

    @Test
    fun `should return a profile for user authenticated when valid username as path parameter`() {
        val profile = ProfileData("mirjahal", "Mini bio", "image.jpg", false)
        val profileBusinessMock = getMock(profileBusiness)

        every { profileBusinessMock.get(any(), any()) } returns profile

        val request = HttpRequest.GET<ProfileDto.Response>("/profiles/${profile.username}").apply {
            header(HttpHeaders.AUTHORIZATION, "${HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER} $token")
        }
        val response = httpClient.toBlocking().exchange(request, ProfileDto.Response::class.java)
        val body = response.body.get()

        response.status shouldBe HttpStatus.OK
        body.username shouldBe profile.username
        body.bio shouldBe profile.bio
        body.image shouldBe profile.image
        body.following shouldBe profile.following
    }

    @Test
    fun `should return a profile for anonymous user when valid username as path parameter`() {
        val profile = ProfileData("mirjahal", "Mini bio", "image.jpg")
        val profileBusinessMock = getMock(profileBusiness)

        every { profileBusinessMock.get(any(), any()) } returns profile

        val request = HttpRequest.GET<ProfileDto.Response>("/profiles/${profile.username}")
        val response = httpClient.toBlocking().exchange(request, ProfileDto.Response::class.java)
        val body = response.body.get()

        response.status shouldBe HttpStatus.OK
        body.username shouldBe profile.username
        body.bio shouldBe profile.bio
        body.image shouldBe profile.image
        body.following shouldBe profile.following
    }

    @Test
    fun `should return profile when follow user by username in path parameter`() {
        val profile = ProfileData("mirjahal", "Mini bio", "image.jpg", true)
        val profileBusinessMock = getMock(profileBusiness)

        every { profileBusinessMock.followUser(any(), any()) } returns profile

        val request = HttpRequest.POST("/profiles/${profile.username}/follow", "").apply {
            header(HttpHeaders.AUTHORIZATION, "${HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER} $token")
        }
        val response = httpClient.toBlocking().exchange(request, ProfileDto.Response::class.java)
        val body = response.body.get()

        response.status shouldBe HttpStatus.CREATED
        response.header(HttpHeaders.LOCATION) shouldBe "/api/profiles/${profile.username}"
        body.username shouldBe profile.username
        body.bio shouldBe profile.bio
        body.image shouldBe profile.image
        body.following shouldBe profile.following
    }

    @Test
    fun `should return profile when unfollow user by username in path parameter`() {
        val profile = ProfileData("mirjahal", "Mini bio", "image.jpg", false)
        val profileBusinessMock = getMock(profileBusiness)

        every { profileBusinessMock.unfollowUser(any(), any()) } returns profile

        val request = HttpRequest.DELETE<ProfileDto.Response>("/profiles/${profile.username}/follow").apply {
            header(HttpHeaders.AUTHORIZATION, "${HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER} $token")
        }
        val response = httpClient.toBlocking().exchange(request, ProfileDto.Response::class.java)
        val body = response.body.get()

        response.status shouldBe HttpStatus.OK
        body.username shouldBe profile.username
        body.bio shouldBe profile.bio
        body.image shouldBe profile.image
        body.following shouldBe profile.following
    }

}