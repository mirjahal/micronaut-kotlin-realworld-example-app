package io.realworld.app.micronaut.infrastructure.web.handler

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("errors")
class Errors(val body: List<String>)