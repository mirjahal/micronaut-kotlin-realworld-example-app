package io.realworld.app.micronaut.application.business

import io.kotlintest.matchers.boolean.shouldBeFalse
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
import io.realworld.app.micronaut.domain.business.ProfileBusiness
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.domain.entity.UserFollow
import io.realworld.app.micronaut.domain.entity.UserFollowKey
import io.realworld.app.micronaut.domain.repository.UserFollowRepository
import java.util.UUID

@MicronautTest
class ProfileBusinessImplTest(
    @Value("\${realworld.token}") private val token: String,
    private val userFollowRepository: UserFollowRepository,
    private val profileBusiness: ProfileBusiness,
    private val userBusiness: UserBusiness
) : AnnotationSpec() {

    @MockBean(UserBusiness::class)
    fun userBusiness() : UserBusiness { return mockk() }

    @MockBean(UserFollowRepository::class)
    fun userFollowRepository() : UserFollowRepository { return mockk(relaxUnitFun = true) }

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

    @Test
    fun `should save user follow relationship when valid username and valid user id`() {
        val followerUser = User(username = "mirjahal", email = "almirjr.87@gmail.com", password = "123456", token = token)
        val followedUser = User(username = "fulano", email = "fulano@gmail.com", password = "abcdef", bio = "Bios", image = "image.jpg")
        val userFollow = UserFollow(UserFollowKey(followerUser, followedUser))
        val userBusinessMock = getMock(userBusiness)
        val userFollowRepositoryMock = getMock(userFollowRepository)

        every { userBusinessMock.findByUsername(any()) } returns followedUser
        every { userBusinessMock.findById(any()) } returns followerUser
        every { userFollowRepositoryMock.save(any<UserFollow>()) } returns userFollow

        val profile = profileBusiness.followUser(followedUser.username, followerUser.id)

        profile.username shouldBe followedUser.username
        profile.bio shouldBe followedUser.bio
        profile.image shouldBe followedUser.image
        profile.following!!.shouldBeTrue()
    }

    @Test
    fun `should delete user follow relationship when valid username and valid user id`() {
        val followerUser = User(username = "mirjahal", email = "almirjr.87@gmail.com", password = "123456", token = token)
        val followedUser = User(username = "fulano", email = "fulano@gmail.com", password = "abcdef", bio = "Bios", image = "image.jpg")
        val userBusinessMock = getMock(userBusiness)

        every { userBusinessMock.findByUsername(any()) } returns followedUser
        every { userBusinessMock.findById(any()) } returns followerUser

        val profile = profileBusiness.unfollowUser(followedUser.username, followerUser.id)

        profile.username shouldBe followedUser.username
        profile.bio shouldBe followedUser.bio
        profile.image shouldBe followedUser.image
        profile.following!!.shouldBeFalse()
    }

}