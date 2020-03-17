package io.realworld.app.micronaut.application.data

import java.time.LocalDateTime

data class ArticleData(
    var slug: String = "",
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    var favorited: Boolean = false,
    var favoritesCount: Int = 0,
    var author: ProfileData? = null
)