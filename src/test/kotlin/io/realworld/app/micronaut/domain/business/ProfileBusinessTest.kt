package io.realworld.app.micronaut.domain.business

import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotlintest.MicronautKotlinTestExtension.getMock
import io.mockk.every
import io.mockk.mockk
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.repository.UserFollowRepository
import java.util.UUID

@MicronautTest
class ProfileBusinessTest(
    private val userBusiness: UserBusiness,
    private val profileBusiness: ProfileBusiness,
    private val userFollowRepository: UserFollowRepository
) : AnnotationSpec() {

    @MockBean(UserBusiness::class)
    fun userBusiness() : UserBusiness { return mockk() }

    @MockBean(UserFollowRepository::class)
    fun userFollowRepository() : UserFollowRepository { return mockk() }

    @Test
    fun `should return profile when valid username and null user id`() {
        val username = "mirjahal"
        val user = User(UUID.randomUUID(), username, "almirjr.87@gmail.com", "hashpassword", "123.456.789", "Mini bio", "image.jpg")
        val userBusinessMock = getMock(userBusiness)
        every { userBusinessMock.findByUsername(any()) } returns user

        val profile = profileBusiness.get(username)

        profile.username shouldBe username
        profile.image shouldBe user.image
        profile.bio shouldBe user.bio
        profile.following shouldBe null
    }

    @Test
    fun `should return profile with following as true when valid username and valid user id`() {
        val username = "fulano"
        val followedUser = User(UUID.randomUUID(), username, "fulano.87@gmail.com", "hashpassword12", "aaa.456.789", "Bios", "selfie.jpg")
        val followerUser = User(UUID.randomUUID(), "mirjahal", "almirjr.87@gmail.com", "hashpassword", "123.456.789", "Mini bio", "image.jpg")
        val userBusinessMock = getMock(userBusiness)
        val userFollowRepositoryMock = getMock(userFollowRepository)
        every { userBusinessMock.findByUsername(any()) } returns followedUser
        every { userBusinessMock.findById(any()) } returns followerUser
        every { userFollowRepositoryMock.findById(any()).isPresent } returns true

        val profile = profileBusiness.get(username, UUID.randomUUID())

        profile.username shouldBe username
        profile.image shouldBe followedUser.image
        profile.bio shouldBe followedUser.bio
        profile.following!!.shouldBeTrue()
    }

}