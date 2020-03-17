package io.realworld.app.micronaut.infrastructure.repository

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.GenericRepository
import io.realworld.app.micronaut.domain.entity.ArticleTag
import io.realworld.app.micronaut.domain.entity.ArticleTagKey
import io.realworld.app.micronaut.domain.repository.ArticleTagRepository

@Repository
interface ArticleTagJpaRepository : GenericRepository<ArticleTag, ArticleTagKey>, ArticleTagRepository