package io.realworld.app.micronaut.domain.business

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotlintest.MicronautKotlinTestExtension.getMock
import io.mockk.every
import io.mockk.mockk
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.domain.exception.ResourceNotFoundException
import io.realworld.app.micronaut.repository.UserRepository
import java.util.Optional
import java.util.UUID

@MicronautTest
class UserBusinessTest(
    private val userBusiness: UserBusiness,
    private val userRepository: UserRepository
) : AnnotationSpec() {

    @MockBean(UserRepository::class)
    fun userRepository(): UserRepository {
        return mockk()
    }

    @Test
    fun `should create a new user when execute save with valid data`() {
        val user = User(UUID.randomUUID(), "Almir Jr.", "almirjr.87@gmail.com", "123456")
        val userRepositoryMock = getMock(userRepository)
        every { userRepositoryMock.save(any<User>()) } returns user

        val userSaved = userBusiness.save(user)

        userSaved.id shouldBe user.id
    }

    @Test
    fun `should return user when search by email`() {
        val optionalUser = Optional.of(
            User(UUID.randomUUID(), "Almir Jr.", "almirjr.87@gmail.com", "123456", "123.456.789")
        )
        val userRepositoryMock = getMock(userRepository)
        every { userRepositoryMock.findByEmail(any()) } returns optionalUser

        val user = userBusiness.findByEmail("almirjr.87@gmail.com")

        optionalUser.get().id shouldBe user.id
    }

    @Test(expected = ResourceNotFoundException::class)
    fun `should throw ResourceNotFoundException when does not find by email`() {
        val userRepositoryMock = getMock(userRepository)
        every { userRepositoryMock.findByEmail(any()) } throws ResourceNotFoundException()

        userBusiness.findByEmail("123456789")
    }

    @Test
    fun `should update user when execute update with valid data`() {
        val optionalUser = Optional.of(
            User(UUID.randomUUID(), "Almir Jr.", "almirjr.87@gmail.com", "123456", "123.456.789")
        )
        val userToUpdate = optionalUser.get().copy(
            username = "Almir Jr.",
            email = "almirjr.87@gmail.com",
            password = "123456",
            bio = "Mini bio",
            image = "image.jpg"
        )

        val userRepositoryMock = getMock(userRepository)
        every { userRepositoryMock.findByEmail(any()) } returns optionalUser
        every { userRepositoryMock.update(any<User>()) } returns userToUpdate

        val updatedUser = userBusiness.update(userToUpdate)

        updatedUser.id shouldBe userToUpdate.id
        updatedUser.username shouldBe userToUpdate.username
        updatedUser.email shouldBe userToUpdate.email
        updatedUser.password shouldBe userToUpdate.password
        updatedUser.bio shouldBe userToUpdate.bio
        updatedUser.image shouldBe userToUpdate.image
        updatedUser.token shouldBe userToUpdate.token
    }

    @Test
    fun `should return user when search by id`() {
        val id = UUID.randomUUID()
        val optionalUser = Optional.of(
            User(id, "Almir Jr.", "almirjr.87@gmail.com", "123456", "123.456.789")
        )
        val userRepositoryMock = getMock(userRepository)
        every { userRepositoryMock.findById(any()) } returns optionalUser

        val user = userBusiness.findById(id)

        optionalUser.get().id shouldBe user.id
    }

}