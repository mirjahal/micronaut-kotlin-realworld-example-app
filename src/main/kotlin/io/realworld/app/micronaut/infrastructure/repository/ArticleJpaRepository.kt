package io.realworld.app.micronaut.infrastructure.repository

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.GenericRepository
import io.realworld.app.micronaut.domain.entity.Article
import io.realworld.app.micronaut.domain.repository.ArticleRepository
import java.util.UUID

@Repository
interface ArticleJpaRepository : GenericRepository<Article, UUID>, ArticleRepository