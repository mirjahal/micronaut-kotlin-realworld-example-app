package io.realworld.app.micronaut.domain.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tag")
data class Tag(
    @Id
    @Column(columnDefinition = "VARBINARY(16)")
    val id: UUID = UUID.randomUUID(),
    val name: String
)