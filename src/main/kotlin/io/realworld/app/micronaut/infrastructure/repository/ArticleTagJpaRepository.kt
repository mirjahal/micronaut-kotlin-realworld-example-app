package io.realworld.app.micronaut.infrastructure.repository

import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.GenericRepository
import io.realworld.app.micronaut.domain.entity.Article
import io.realworld.app.micronaut.domain.entity.ArticleTag
import io.realworld.app.micronaut.domain.entity.ArticleTagKey
import io.realworld.app.micronaut.domain.repository.ArticleTagRepository

@Repository
interface ArticleTagJpaRepository : GenericRepository<ArticleTag, ArticleTagKey>, ArticleTagRepository {
    @Query("FROM ArticleTag at WHERE at.articleTagKey.article = :article")
    override fun findByArticle(article: Article) : List<ArticleTag>
}