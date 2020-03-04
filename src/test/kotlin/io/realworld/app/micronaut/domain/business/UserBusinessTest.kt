package io.realworld.app.micronaut.domain.business

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotlintest.MicronautKotlinTestExtension.getMock
import io.mockk.every
import io.mockk.mockk
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.repository.UserRepository
import java.util.UUID

@MicronautTest
class UserBusinessTest(
    private val userBusiness: UserBusiness,
    private val userRepository: UserRepository
) : AnnotationSpec() {

    @MockBean(UserRepository::class)
    fun userRepository() : UserRepository { return mockk() }

    @Test
    fun `should create a new user when execute save with valid data`() {
        val user = User(UUID.randomUUID(), "Almir Jr.", "almirjr.87@gmail.com", "123456")
        val userRepositoryMock = getMock(userRepository)
        every { userRepositoryMock.save(any<User>()) } returns user

        val userSaved = userBusiness.save(user)

        userSaved.id shouldBe user.id
    }

}