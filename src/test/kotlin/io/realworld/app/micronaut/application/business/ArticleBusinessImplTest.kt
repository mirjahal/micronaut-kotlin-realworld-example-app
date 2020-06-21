package io.realworld.app.micronaut.application.business

import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
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
import io.realworld.app.micronaut.domain.entity.*
import io.realworld.app.micronaut.domain.repository.ArticleRepository
import io.realworld.app.micronaut.domain.repository.ArticleTagRepository
import io.realworld.app.micronaut.domain.repository.ArticleUserRepository
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@MicronautTest
class ArticleBusinessImplTest(
    private val userBusiness: UserBusiness,
    private val articleBusiness: ArticleBusiness,
    private val articleRepository: ArticleRepository,
    private val articleTagRepository: ArticleTagRepository,
    private val articleUserRepository: ArticleUserRepository
) : AnnotationSpec() {

    @MockBean(ArticleRepository::class)
    fun articleRepository(): ArticleRepository { return mockk() }

    @MockBean(ArticleTagRepository::class)
    fun articleTagRepository(): ArticleTagRepository { return mockk() }

    @MockBean(ArticleUserRepository::class)
    fun articleUserRepository(): ArticleUserRepository { return mockk() }

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
        val articleTag1 = ArticleTag(ArticleTagKey(article, Tag(name = "dragons")))
        val articleTag2 = ArticleTag(ArticleTagKey(article, Tag(name = "training")))

        val userBusinessMock = getMock(userBusiness)
        val articleRepositoryMock = getMock(articleRepository)
        val articleTagRepositoryMock = getMock(articleTagRepository)
        val articleUserRepositoryMock = getMock(articleUserRepository)
        every { userBusinessMock.findById(any()) } returns user
        every { articleRepositoryMock.save(any()) } returns article
        every { articleTagRepositoryMock.saveAll(any()) } returns listOf(articleTag1, articleTag2)
        every { articleUserRepositoryMock.findById(any()) } returns Optional.empty()
        every { articleUserRepositoryMock.countByArticleUserKeyArticleId(any()) } returns 0

        val articleDataSaved = articleBusiness.save(articleData, userAuthenticatedId)

        articleDataSaved.slug shouldBe article.slug
        articleDataSaved.title shouldBe article.title
        articleDataSaved.description shouldBe article.description
        articleDataSaved.body shouldBe article.body
        articleDataSaved.tagList shouldContainAll articleData.tagList
        articleDataSaved.createdAt shouldBe article.createdAt
        articleDataSaved.updatedAt shouldBe article.updatedAt
        articleData.favorited.shouldBeFalse()
        articleData.favoritesCount shouldBe 0
        articleDataSaved.author?.username shouldBe article.author.username
    }

    @Test
    fun `should save a favorite article for user when execute favorite with valid slug`() {
        val userAuthenticatedId = UUID.randomUUID()
        val currentLocalDateTime = LocalDateTime.now()
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = "123456")
        val article = Article(
            slug = "how-to-train-your-dragon",
            title = "How to train your dragon",
            description = "Ever wonder how?",
            body = "It takes a Jacobian",
            createdAt = currentLocalDateTime,
            updatedAt = currentLocalDateTime,
            author = user
        )
        val articleUser = ArticleUser(ArticleUserKey(article, user))
        val articleTag = ArticleTag(ArticleTagKey(article, Tag(name = "Tag name")))

        val userBusinessMock = getMock(userBusiness)
        val articleRepositoryMock = getMock(articleRepository)
        val articleUserRepositoryMock = getMock(articleUserRepository)
        val articleTagRepositoryMock = getMock(articleTagRepository)
        every { userBusinessMock.findById(any()) } returns user
        every { articleRepositoryMock.findBySlug(any()) } returns Optional.of(article)
        every { articleUserRepositoryMock.save(any()) } returns articleUser
        every { articleUserRepositoryMock.findById(any()) } returns Optional.of(articleUser)
        every { articleTagRepositoryMock.findByArticle(any()) } returns listOf(articleTag)
        every { articleUserRepositoryMock.countByArticleUserKeyArticleId(any()) } returns 1

        val articleData = articleBusiness.favorite(article.slug, userAuthenticatedId)

        articleData.slug shouldBe article.slug
        articleData.title shouldBe article.title
        articleData.description shouldBe article.description
        articleData.body shouldBe article.body
        articleData.createdAt shouldBe article.createdAt
        articleData.updatedAt shouldBe article.updatedAt
        articleData.favorited.shouldBeTrue()
        articleData.favoritesCount shouldBe 1
        articleData.author?.username shouldBe article.author.username
    }

    @Test
    fun `should delete a favorite article for user when execute unfavorite with valid slug`() {
        val userAuthenticatedId = UUID.randomUUID()
        val currentLocalDateTime = LocalDateTime.now()
        val user = User(username = "Almir Jr.", email = "almirjr.87@gmail.com", password = "123456")
        val article = Article(
            slug = "how-to-train-your-dragon",
            title = "How to train your dragon",
            description = "Ever wonder how?",
            body = "It takes a Jacobian",
            createdAt = currentLocalDateTime,
            updatedAt = currentLocalDateTime,
            author = user
        )

        val userBusinessMock = getMock(userBusiness)
        val articleRepositoryMock = getMock(articleRepository)
        val articleTagRepositoryMock = getMock(articleTagRepository)
        val articleUserRepositoryMock = getMock(articleUserRepository)

        every { userBusinessMock.findById(any()) } returns user
        every { articleRepositoryMock.findBySlug(any()) } returns Optional.of(article)
        every { articleRepositoryMock.delete(any()) } returns Unit
        every { articleTagRepositoryMock.findByArticle(any()) } returns listOf()
        every { articleUserRepositoryMock.findById(any()) } returns Optional.empty()
        every { articleUserRepositoryMock.countByArticleUserKeyArticleId(any()) } returns 0

        val articleData = articleBusiness.unfavorite(article.slug, userAuthenticatedId)

        articleData.slug shouldBe article.slug
        articleData.title shouldBe article.title
        articleData.description shouldBe article.description
        articleData.body shouldBe article.body
        articleData.createdAt shouldBe article.createdAt
        articleData.updatedAt shouldBe article.updatedAt
        articleData.favorited.shouldBeFalse()
        articleData.favoritesCount shouldBe 0
        articleData.author?.username shouldBe article.author.username
    }

}