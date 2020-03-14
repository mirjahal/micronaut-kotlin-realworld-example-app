package io.realworld.app.micronaut.infrastructure

import io.micronaut.runtime.Micronaut

object ApplicationRunner {

    fun run(args: Array<String>) {
        Micronaut.build(*args)
            .packages("io.realworld.app.micronaut")
            .mainClass(ApplicationRunner.javaClass)
            .start()
    }
}