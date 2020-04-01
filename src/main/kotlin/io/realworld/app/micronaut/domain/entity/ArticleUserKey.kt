package io.realworld.app.micronaut.domain.entity

import java.io.Serializable
import javax.persistence.Embeddable
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Embeddable
data class ArticleUserKey(
    @ManyToOne @JoinColumn(name = "article_id") val article: Article,
    @ManyToOne @JoinColumn(name = "user_id") val user: User
) : Serializable