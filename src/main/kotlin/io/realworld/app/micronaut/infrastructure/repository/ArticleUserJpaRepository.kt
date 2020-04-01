package io.realworld.app.micronaut.infrastructure.repository

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.GenericRepository
import io.realworld.app.micronaut.domain.entity.ArticleUser
import io.realworld.app.micronaut.domain.entity.ArticleUserKey
import io.realworld.app.micronaut.domain.repository.ArticleUserRepository
import java.util.UUID

@Repository
interface ArticleUserJpaRepository : GenericRepository<ArticleUser, ArticleUserKey>, ArticleUserRepository {
    override fun countByArticleUserKeyArticleId(articleId: UUID): Int
}