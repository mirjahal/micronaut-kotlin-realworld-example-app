package io.realworld.app.micronaut.domain.entity

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "article_user")
data class ArticleUser(
    @EmbeddedId val articleUserKey: ArticleUserKey
)