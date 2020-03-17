package io.realworld.app.micronaut.application.business

import io.realworld.app.micronaut.application.data.ArticleData
import io.realworld.app.micronaut.application.data.ProfileData
import io.realworld.app.micronaut.application.providers.SlugProvider
import io.realworld.app.micronaut.domain.business.ArticleBusiness
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.entity.Article
import io.realworld.app.micronaut.domain.entity.ArticleTag
import io.realworld.app.micronaut.domain.entity.ArticleTagKey
import io.realworld.app.micronaut.domain.entity.Tag
import io.realworld.app.micronaut.domain.repository.ArticleRepository
import io.realworld.app.micronaut.domain.repository.ArticleTagRepository
import io.realworld.app.micronaut.domain.repository.TagRepository
import java.util.UUID
import javax.inject.Singleton

@Singleton
class ArticleBusinessImpl(
    private val slugProvider: SlugProvider,
    private val userBusiness: UserBusiness,
    private val tagRepository: TagRepository,
    private val articleRepository: ArticleRepository,
    private val articleTagRepository: ArticleTagRepository
) : ArticleBusiness {

    override fun save(articleData: ArticleData, userId: UUID): ArticleData {
        val savedArticle = saveArticle(articleData, userId)
        val savedTags = saveTags(savedArticle, articleData.tagList)

        return ArticleData(
            slug = savedArticle.slug,
            title = savedArticle.title,
            description = savedArticle.description,
            body = savedArticle.body,
            tagList = savedTags.map { tag -> tag.name },
            createdAt = savedArticle.createdAt,
            updatedAt = savedArticle.updatedAt,
            author = ProfileData(
                username = savedArticle.author.username,
                bio = savedArticle.author.bio,
                image = savedArticle.author.image,
                following = false
            )
        )
    }

    private fun saveArticle(articleData: ArticleData, userId: UUID) : Article {
        val user = userBusiness.findById(userId)
        val article = Article(
            slug = slugProvider.slug(articleData.title),
            title = articleData.title,
            description = articleData.description,
            body = articleData.body,
            createdAt = articleData.createdAt,
            updatedAt = articleData.updatedAt,
            author = user
        )

        return articleRepository.save(article)
    }

    private fun saveTags(article: Article, tagList: List<String>) : List<Tag> {
        val tags = tagList.map { tagName ->
            Tag(name = tagName)
        }

        return tagRepository.saveAll(tags).also { savedTags ->
            saveArticleTags(article, savedTags)
        }
    }

    private fun saveArticleTags(article: Article, tags: List<Tag>) {
        val articleTags = tags.map { tag ->
            ArticleTag(ArticleTagKey(article, tag))
        }

        articleTagRepository.saveAll(articleTags)
    }

}