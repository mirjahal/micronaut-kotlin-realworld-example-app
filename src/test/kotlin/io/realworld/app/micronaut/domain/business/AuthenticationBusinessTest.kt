package io.realworld.app.micronaut.domain.business

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.context.annotation.Value
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.providers.PasswordEncoder
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotlintest.MicronautKotlinTestExtension.getMock
import io.mockk.every
import io.mockk.mockk
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.repository.UserRepository
import java.util.Optional

@MicronautTest
class AuthenticationBusinessTest(
    @Value("\${realworld.token}") private val token: String,
    private val authenticationBusiness: AuthenticationBusiness,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
): AnnotationSpec() {

    @MockBean(UserRepository::class)
    fun userRepository() : UserRepository { return mockk() }

    @Test
    fun `should return user with valid token when valid credentials`() {
        val rawPassword = "123456"
        val encodedPassword = passwordEncoder.encode(rawPassword)
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = encodedPassword, token = token)
        val userRepositoryMock = getMock(userRepository)

        every { userRepositoryMock.findByEmail(any()) } returns Optional.of(user)
        every { userRepositoryMock.update(any<User>()) } returns user

        val authenticatedUser = authenticationBusiness.authenticate(user.email, rawPassword)

        authenticatedUser.id shouldBe user.id
        authenticatedUser.token shouldBe user.token
    }

    @Test(expected = AuthenticationException::class)
    fun `should throw AuthenticationException when authentication error`() {
        val userRepositoryMock = getMock(userRepository)

        every { userRepositoryMock.findByEmail(any()) } throws AuthenticationException()

        authenticationBusiness.authenticate("almirjr.87@gmail.com", "123456")
    }
}