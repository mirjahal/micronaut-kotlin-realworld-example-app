package io.realworld.app.micronaut.domain.repository

import io.realworld.app.micronaut.domain.entity.ArticleTag
import io.realworld.app.micronaut.domain.entity.ArticleTagKey

interface ArticleTagRepository : GenericEntityRepository<ArticleTag, ArticleTagKey>