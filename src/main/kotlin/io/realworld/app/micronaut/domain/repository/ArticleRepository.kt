package io.realworld.app.micronaut.domain.repository

import io.realworld.app.micronaut.domain.entity.Article
import java.util.Optional
import java.util.UUID

interface ArticleRepository : GenericEntityRepository<Article, UUID> {
    fun findBySlug(slug: String): Optional<Article>
}