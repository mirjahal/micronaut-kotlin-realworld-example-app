package io.realworld.app.micronaut.domain.entity

import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.FetchType.LAZY

@Entity
@Table(name = "article")
data class Article(
    @Id @Column(columnDefinition = "VARBINARY(16)") val id: UUID = UUID.randomUUID(),
    @ManyToOne(fetch = LAZY) @JoinColumn(name = "user_id") val author: User,
    @Column(name = "created_at") val createdAt: LocalDateTime,
    @Column(name = "updated_at") val updatedAt: LocalDateTime,
    val slug: String,
    val title: String,
    val description: String,
    val body: String
)