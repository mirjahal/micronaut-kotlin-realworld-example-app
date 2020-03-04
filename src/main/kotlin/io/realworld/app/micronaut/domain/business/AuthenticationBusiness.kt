package io.realworld.app.micronaut.domain.business

import io.realworld.app.micronaut.domain.entity.User

interface AuthenticationBusiness {

    fun authenticate(email: String, rawPassword: String) : User

}