package io.realworld.app.micronaut.application.business

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotlintest.MicronautKotlinTestExtension.getMock
import io.mockk.every
import io.mockk.mockk
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.application.exception.ResourceNotFoundException
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.repository.UserRepository
import java.util.Optional

@MicronautTest
class UserBusinessImplTest(
    private val userBusiness: UserBusiness,
    private val userRepository: UserRepository
) : AnnotationSpec() {

    @MockBean(UserRepository::class)
    fun userRepository(): UserRepository {
        return mockk()
    }

    @Test
    fun `should create a new user when execute save with valid data`() {
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = "123456")
        val userRepositoryMock = getMock(userRepository)

        every { userRepositoryMock.save(any<User>()) } returns user

        val savedUser = userBusiness.save(user)

        savedUser.id shouldBe user.id
        savedUser.username shouldBe user.username
        savedUser.email shouldBe user.email
        savedUser.password shouldBe user.password
    }

    @Test
    fun `should return user when search by email`() {
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = "123456")
        val userRepositoryMock = getMock(userRepository)

        every { userRepositoryMock.findByEmail(any()) } returns Optional.of(user)

        val savedUser = userBusiness.findByEmail("almirjr.87@gmail.com")

        savedUser.id shouldBe user.id
        savedUser.username shouldBe user.username
        savedUser.email shouldBe user.email
        savedUser.password shouldBe user.password
    }

    @Test(expected = ResourceNotFoundException::class)
    fun `should throw ResourceNotFoundException when does not find by email`() {
        val userRepositoryMock = getMock(userRepository)

        every { userRepositoryMock.findByEmail(any()) } throws ResourceNotFoundException()

        userBusiness.findByEmail("ze@gmail.com")
    }

    @Test
    fun `should update user when execute update with valid data`() {
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = "123456")
        val userToUpdate = user.copy(bio = "Mini bio", image = "image.jpg")
        val userRepositoryMock = getMock(userRepository)

        every { userRepositoryMock.findByEmail(any()) } returns Optional.of(user)
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
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = "123456")
        val userRepositoryMock = getMock(userRepository)

        every { userRepositoryMock.findById(any()) } returns Optional.of(user)

        val userById = userBusiness.findById(user.id)

        userById.id shouldBe user.id
        userById.username shouldBe user.username
        userById.email shouldBe user.email
        userById.password shouldBe user.password
    }

}