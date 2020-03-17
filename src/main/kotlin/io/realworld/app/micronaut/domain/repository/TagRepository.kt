package io.realworld.app.micronaut.domain.repository

import io.realworld.app.micronaut.domain.entity.Tag
import java.util.UUID

interface TagRepository : GenericEntityRepository<Tag, UUID>