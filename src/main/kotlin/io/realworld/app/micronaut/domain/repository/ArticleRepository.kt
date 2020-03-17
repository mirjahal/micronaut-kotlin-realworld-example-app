package io.realworld.app.micronaut.domain.repository

import io.realworld.app.micronaut.domain.entity.Article
import java.util.UUID

interface ArticleRepository : GenericEntityRepository<Article, UUID>