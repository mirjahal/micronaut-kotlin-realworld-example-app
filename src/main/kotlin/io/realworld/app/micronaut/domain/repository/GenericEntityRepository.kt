package io.realworld.app.micronaut.domain.repository

import java.util.Optional

interface GenericEntityRepository<T, ID> {

    fun findById(id: ID): Optional<T>
    fun save(entity: T): T
    fun saveAll(entities: List<T>):  List<T>
    fun update(entity: T): T
    fun delete(entity: T)

}