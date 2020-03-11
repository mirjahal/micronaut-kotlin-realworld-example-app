package io.realworld.app.micronaut.repository

import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.test.annotation.MicronautTest
import io.realworld.app.micronaut.domain.entity.User

@MicronautTest
class UserRepositoryTest(
    private val userRepository: UserRepository
) : AnnotationSpec() {

    @Test
    fun `should be user data is persisted when execute save`() {
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = "123456")

        val savedUser = userRepository.save(user)

        savedUser.shouldNotBeNull()
        savedUser.id shouldBe user.id
        savedUser.username shouldBe user.username
    }

}