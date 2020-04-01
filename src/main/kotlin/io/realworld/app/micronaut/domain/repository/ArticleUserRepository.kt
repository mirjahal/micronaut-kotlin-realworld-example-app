package io.realworld.app.micronaut.domain.repository

import io.realworld.app.micronaut.domain.entity.ArticleUser
import io.realworld.app.micronaut.domain.entity.ArticleUserKey
import java.util.UUID

interface ArticleUserRepository : GenericEntityRepository<ArticleUser, ArticleUserKey> {
    fun countByArticleUserKeyArticleId(articleId: UUID) : Int
}