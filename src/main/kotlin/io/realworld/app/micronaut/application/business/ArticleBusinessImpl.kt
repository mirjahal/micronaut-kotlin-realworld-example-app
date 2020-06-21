package io.realworld.app.micronaut.application.business

import io.realworld.app.micronaut.application.data.ArticleData
import io.realworld.app.micronaut.application.data.ProfileData
import io.realworld.app.micronaut.application.exception.ResourceNotFoundException
import io.realworld.app.micronaut.application.providers.SlugProvider
import io.realworld.app.micronaut.domain.business.ArticleBusiness
import io.realworld.app.micronaut.domain.business.ProfileBusiness
import io.realworld.app.micronaut.domain.business.UserBusiness
import io.realworld.app.micronaut.domain.entity.*
import io.realworld.app.micronaut.domain.repository.ArticleRepository
import io.realworld.app.micronaut.domain.repository.ArticleTagRepository
import io.realworld.app.micronaut.domain.repository.ArticleUserRepository
import io.realworld.app.micronaut.domain.repository.TagRepository
import java.util.UUID
import javax.inject.Singleton

@Singleton
class ArticleBusinessImpl(
    private val slugProvider: SlugProvider,
    private val userBusiness: UserBusiness,
    private val profileBusiness: ProfileBusiness,
    private val tagRepository: TagRepository,
    private val articleRepository: ArticleRepository,
    private val articleTagRepository: ArticleTagRepository,
    private val articleUserRepository: ArticleUserRepository
) : ArticleBusiness {

    override fun save(articleData: ArticleData, userId: UUID): ArticleData {
        val user = userBusiness.findById(userId)
        val savedArticle = saveArticle(articleData, user)
        val savedArticleTags = saveArticleTags(savedArticle, articleData.tagList)

        return buildArticleData(savedArticle, savedArticleTags, user)
    }

    override fun favorite(slug: String, userId: UUID): ArticleData {
        val user = userBusiness.findById(userId)
        val article = articleRepository
            .findBySlug(slug)
            .orElseThrow { ResourceNotFoundException() }

        articleUserRepository.save(ArticleUser(ArticleUserKey(article, user)))
        val articleTags = articleTagRepository.findByArticle(article)

        return buildArticleData(article, articleTags, user)
    }

    override fun unfavorite(slug: String, userId: UUID): ArticleData {
        val user = userBusiness.findById(userId)
        val article = articleRepository
            .findBySlug(slug)
            .orElseThrow { ResourceNotFoundException() }
        val articleTags = articleTagRepository.findByArticle(article)

        articleRepository.delete(article)

        return buildArticleData(article, articleTags, user)
    }

    private fun saveArticle(articleData: ArticleData, user: User) : Article {
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

    private fun saveArticleTags(article: Article, tagList: List<String>) : List<ArticleTag> {
        saveTags(tagList).also { savedTags ->
            val articleTags = savedTags.map { tag ->
                ArticleTag(ArticleTagKey(article, tag))
            }

            return articleTagRepository.saveAll(articleTags)
        }
    }

    private fun saveTags(tagList: List<String>) : List<Tag> {
        val tags = tagList.map { tagName ->
            Tag(name = tagName)
        }

        return tagRepository.saveAll(tags)
    }

    private fun buildArticleData(article: Article, articleTags: List<ArticleTag>, user: User) : ArticleData {
        val favoriteArticle = articleUserRepository.findById(ArticleUserKey(article, user)).isPresent
        val favoritesCount = articleUserRepository.countByArticleUserKeyArticleId(article.id)
        val followingAuthor = profileBusiness.isFollowingUser(article.author, user.id)

        return ArticleData(
            slug = article.slug,
            title = article.title,
            description = article.description,
            body = article.body,
            tagList = articleTags.map { articleTag -> articleTag.tagName() },
            createdAt = article.createdAt,
            updatedAt = article.updatedAt,
            favorited = favoriteArticle,
            favoritesCount = favoritesCount,
            author = ProfileData(
                username = article.author.username,
                bio = article.author.bio,
                image = article.author.image,
                following = followingAuthor
            )
        )
    }

}