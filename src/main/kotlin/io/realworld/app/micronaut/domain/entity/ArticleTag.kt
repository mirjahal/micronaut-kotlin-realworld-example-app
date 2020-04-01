package io.realworld.app.micronaut.domain.entity

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Transient

@Entity
@Table(name = "article_tag")
data class ArticleTag(
    @EmbeddedId val articleTagKey: ArticleTagKey
) {
    @Transient
    fun tagName() = articleTagKey.tag.name
}