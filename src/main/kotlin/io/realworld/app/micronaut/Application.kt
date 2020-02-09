package io.realworld.app.micronaut

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("io.realworld.app.micronaut")
                .mainClass(Application.javaClass)
                .start()
    }
}