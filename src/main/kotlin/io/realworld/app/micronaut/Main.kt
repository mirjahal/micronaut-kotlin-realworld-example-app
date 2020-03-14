package io.realworld.app.micronaut

import io.realworld.app.micronaut.infrastructure.ApplicationRunner

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        ApplicationRunner.run(args)
    }
}