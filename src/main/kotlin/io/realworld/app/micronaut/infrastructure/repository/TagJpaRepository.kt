package io.realworld.app.micronaut.infrastructure.repository

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.GenericRepository
import io.realworld.app.micronaut.domain.entity.Tag
import io.realworld.app.micronaut.domain.repository.TagRepository
import java.util.UUID

@Repository
interface TagJpaRepository : GenericRepository<Tag, UUID>, TagRepository