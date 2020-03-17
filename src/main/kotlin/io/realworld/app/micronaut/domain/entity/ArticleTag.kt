package io.realworld.app.micronaut.domain.entity

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "article_tag")
data class ArticleTag(
    @EmbeddedId
    val articleTagKey: ArticleTagKey
)