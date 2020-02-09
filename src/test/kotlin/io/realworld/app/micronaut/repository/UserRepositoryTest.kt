package io.realworld.app.micronaut.repository

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.test.annotation.MicronautTest
import io.realworld.app.micronaut.domain.entity.User
import java.util.UUID

@MicronautTest
class UserRepositoryTest(
    private val userRepository: UserRepository
) : AnnotationSpec() {

    @Test
    fun `given a valid User entity when execute save method then user data is persisted`() {
        val user = User(UUID.randomUUID(), "Almir Jr.", "almirjr.87@gmail.com", "123456")

        userRepository.save(user)
        val userById = userRepository.findById(user.id)

        userById.isPresent shouldBe true
        userById.get().id shouldBe user.id
        userById.get().username shouldBe user.username
    }

}