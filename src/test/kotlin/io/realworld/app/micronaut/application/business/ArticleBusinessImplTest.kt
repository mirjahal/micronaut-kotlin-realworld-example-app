package io.realworld.app.micronaut.application.business

import io.kotlintest.matchers.collections.shouldContainAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotlintest.MicronautKotlinTestExtension.getMock
import io.mockk.every
import io.mockk.mockk
import io.realworld.app.micronaut.application.data.ArticleData
import io.realworld.app.micronaut.domain.business.ArticleBusiness
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.entity.Article
import io.realworld.app.micronaut.domain.entity.User
import io.realworld.app.micronaut.domain.repository.ArticleRepository
import java.time.LocalDateTime
import java.util.UUID

@MicronautTest
class ArticleBusinessImplTest(
    private val userBusiness: UserBusiness,
    private val articleBusiness: ArticleBusiness,
    private val articleRepository: ArticleRepository
) : AnnotationSpec() {

    @MockBean(ArticleRepository::class)
    fun articleRepository(): ArticleRepository { return mockk() }

    @MockBean(UserBusiness::class)
    fun userBusiness(): UserBusiness { return mockk() }

    @Test
    fun `should save a new article when execute save with valid data`() {
        val userAuthenticatedId = UUID.randomUUID()
        val currentLocalDateTime = LocalDateTime.now()
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = "123456")
        val articleData = ArticleData(
            title = "How to train your dragon",
            description = "Ever wonder how?",
            body = "It takes a Jacobian",
            tagList = listOf("dragons", "training"),
            createdAt = currentLocalDateTime,
            updatedAt = currentLocalDateTime
        )
        val article = Article(
            slug = "how-to-train-your-dragon",
            title = articleData.title,
            description = articleData.description,
            body = articleData.body,
            createdAt = articleData.createdAt,
            updatedAt = articleData.updatedAt,
            author = user
        )

        val userBusinessMock = getMock(userBusiness)
        val articleRepositoryMock = getMock(articleRepository)
        every { userBusinessMock.findById(any()) } returns user
        every { articleRepositoryMock.save(any()) } returns article

        val articleDataSaved = articleBusiness.save(articleData, userAuthenticatedId)

        articleDataSaved.slug shouldBe article.slug
        articleDataSaved.title shouldBe article.title
        articleDataSaved.description shouldBe article.description
        articleDataSaved.body shouldBe article.body
        articleDataSaved.tagList shouldContainAll articleData.tagList
        articleDataSaved.createdAt shouldBe article.createdAt
        articleDataSaved.updatedAt shouldBe article.updatedAt
        articleDataSaved.author?.username shouldBe article.author.username
    }

}