package io.realworld.app.micronaut.domain.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user")
data class User(
    @Id
    @Column(columnDefinition = "VARBINARY(16)")
    val id: UUID = UUID.randomUUID(),
    val username: String,
    val email: String,
    var password: String,
    var token: String? = null,
    val bio: String? = null,
    val image: String? = null
)