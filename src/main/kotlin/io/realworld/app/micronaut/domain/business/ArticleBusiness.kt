package io.realworld.app.micronaut.domain.business

import io.realworld.app.micronaut.application.data.ArticleData
import java.util.UUID

interface ArticleBusiness {

    fun save(articleData: ArticleData, userId: UUID): ArticleData
    fun favorite(slug: String, userId: UUID): ArticleData
    fun unfavorite(slug: String, userId: UUID): ArticleData

}
