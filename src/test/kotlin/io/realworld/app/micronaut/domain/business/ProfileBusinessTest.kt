package io.realworld.app.micronaut.domain.business

import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.context.annotation.Value
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
    @Value("\${realworld.token}") private val token: String,
    private val userFollowRepository: UserFollowRepository,
    private val profileBusiness: ProfileBusiness,
    private val userBusiness: UserBusiness
) : AnnotationSpec() {

    @MockBean(UserBusiness::class)
    fun userBusiness() : UserBusiness { return mockk() }

    @MockBean(UserFollowRepository::class)
    fun userFollowRepository() : UserFollowRepository { return mockk() }

    @Test
    fun `should return profile when valid username and null user id`() {
        val user = User(
            username = "mirjahal",
            email = "almirjr.87@gmail.com",
            password = "hashpassword",
            token = token,
            bio = "Mini bio",
            image = "image.jpg")
        val userBusinessMock = getMock(userBusiness)
        every { userBusinessMock.findByUsername(any()) } returns user

        val profile = profileBusiness.get(user.username)

        profile.username shouldBe user.username
        profile.image shouldBe user.image
        profile.bio shouldBe user.bio
        profile.following.shouldBeNull()
    }

    @Test
    fun `should return profile with following as true when valid username and valid user id`() {
        val followerUser = User(username = "mirjahal", email = "almirjr.87@gmail.com", password = "123456", token = token)
        val followedUser = User(username = "fulano", email = "fulano@gmail.com", password = "abcdef", bio = "Bios", image = "image.jpg")
        val userBusinessMock = getMock(userBusiness)
        val userFollowRepositoryMock = getMock(userFollowRepository)
        every { userBusinessMock.findByUsername(any()) } returns followedUser
        every { userBusinessMock.findById(any()) } returns followerUser
        every { userFollowRepositoryMock.findById(any()).isPresent } returns true

        val profile = profileBusiness.get(followedUser.username, UUID.randomUUID())

        profile.username shouldBe followedUser.username
        profile.image shouldBe followedUser.image
        profile.bio shouldBe followedUser.bio
        profile.following!!.shouldBeTrue()
    }

}