package io.realworld.app.micronaut.infrastructure.web.controller

import io.kotlintest.matchers.collections.shouldContainAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpHeaderValues
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.kotlintest.MicronautKotlinTestExtension.getMock
import io.mockk.every
import io.mockk.mockk
import io.realworld.app.micronaut.application.data.ArticleData
import io.realworld.app.micronaut.application.data.ProfileData
import io.realworld.app.micronaut.domain.business.ArticleBusiness
import io.realworld.app.micronaut.infrastructure.web.dto.ArticleDto
import java.time.LocalDateTime

@MicronautTest
class ArticlesControllerTest(
    @Client("/api") private val httpClient: HttpClient,
    @Value("\${realworld.token}") private val token: String,
    private val articleBusiness: ArticleBusiness
) : AnnotationSpec() {

    @MockBean(ArticleBusiness::class)
    fun articleBusiness() : ArticleBusiness { return mockk() }

    @Test
    fun `should create a new article when valid data`() {
        val currentLocalDateTime = LocalDateTime.now()
        val articleData = ArticleData(
            slug = "how-to-train-your-dragon",
            title = "How to train your dragon",
            description = "Ever wonder how?",
            body = "It takes a Jacobian",
            tagList = listOf("dragons", "training"),
            createdAt = currentLocalDateTime,
            updatedAt = currentLocalDateTime,
            author = ProfileData(username = "Almir Jr.", bio = "Mini bio", image = "selfie.png", following = false)
        )
        val articleBusinessMock = getMock(articleBusiness)

        every { articleBusinessMock.save(any(), any()) } returns articleData

        val data = ArticleDto.Request.Create(articleData.title, articleData.description, articleData.body, articleData.tagList)
        val request = HttpRequest.POST("/articles", data).apply {
            header(HttpHeaders.AUTHORIZATION, "${HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER} $token")
        }
        val response = httpClient.toBlocking().exchange(request, ArticleDto.Response.Single::class.java)
        val body = response.body.get()

        response.status shouldBe HttpStatus.CREATED
        response.header(HttpHeaders.LOCATION) shouldBe "/api/articles/${articleData.slug}"
        body.slug shouldBe articleData.slug
        body.title shouldBe articleData.title
        body.description shouldBe articleData.description
        body.body shouldBe articleData.body
        body.tagList shouldContainAll articleData.tagList
        body.createdAt shouldBe articleData.createdAt
        body.updatedAt shouldBe articleData.updatedAt
        body.author.username shouldBe articleData.author?.username
    }

    @Test
    fun `should favorited a article when valid slug as path parameter`() {
        val currentLocalDateTime = LocalDateTime.now()
        val articleData = ArticleData(
            slug = "how-to-train-your-dragon",
            title = "How to train your dragon",
            description = "Ever wonder how?",
            body = "It takes a Jacobian",
            tagList = listOf("dragons", "training"),
            createdAt = currentLocalDateTime,
            updatedAt = currentLocalDateTime,
            author = ProfileData(username = "Almir Jr.", bio = "Mini bio", image = "selfie.png", following = false)
        )
        val articleBusinessMock = getMock(articleBusiness)

        every { articleBusinessMock.favorite(any(), any()) } returns articleData

        val request = HttpRequest.POST("/articles/${articleData.slug}/favorite", "").apply {
            header(HttpHeaders.AUTHORIZATION, "${HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER} $token")
        }
        val response = httpClient.toBlocking().exchange(request, ArticleDto.Response.Single::class.java)
        val body = response.body.get()

        response.status shouldBe HttpStatus.CREATED
        body.slug shouldBe articleData.slug
        body.title shouldBe articleData.title
        body.description shouldBe articleData.description
        body.body shouldBe articleData.body
        body.tagList shouldContainAll articleData.tagList
        body.createdAt shouldBe articleData.createdAt
        body.updatedAt shouldBe articleData.updatedAt
        body.author.username shouldBe articleData.author?.username
    }

}