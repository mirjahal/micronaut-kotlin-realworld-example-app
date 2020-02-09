package io.realworld.app.micronaut.domain.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(
    @Id
    @Column(columnDefinition = "VARBINARY(16)")
    val id: UUID = UUID.randomUUID(),
    val username: String,
    val email: String,
    val password: String,
    val token: String? = null,
    val bio: String? = null,
    val image: String? = null
)