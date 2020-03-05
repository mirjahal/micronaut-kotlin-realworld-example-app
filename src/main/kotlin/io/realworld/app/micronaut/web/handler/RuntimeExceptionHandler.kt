package io.realworld.app.micronaut.web.handler

import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton

@Produces
@Singleton
@Requirements(Requires(classes = [RuntimeException::class, ExceptionHandler::class]))
class RuntimeExceptionHandler : ExceptionHandler<RuntimeException, HttpResponse<*>> {

    override fun handle(request: HttpRequest<*>, exception: RuntimeException): HttpResponse<*> {
        return HttpResponse
            .status<Errors>(HttpStatus.BAD_REQUEST)
            .body(Errors(listOf(exception.localizedMessage)))
    }

}