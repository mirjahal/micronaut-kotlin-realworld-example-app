package io.realworld.app.micronaut.infrastructure.extensions

import java.util.UUID

fun String.toUUID() = UUID.fromString(this)