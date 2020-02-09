package io.realworld.app.micronaut.web.handler

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.validation.exceptions.ConstraintExceptionHandler
import org.hibernate.validator.internal.engine.path.PathImpl
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Produces
@Singleton
@Requirements(Requires(classes = [ConstraintViolationException::class, ExceptionHandler::class]))
@Replaces(ConstraintExceptionHandler::class)
class ConstraintViolationExceptionHandler : ExceptionHandler<ConstraintViolationException, HttpResponse<*>> {

    override fun handle(request: HttpRequest<*>, exception: ConstraintViolationException): HttpResponse<*> {
        val body = ArrayList<String>()

        exception.constraintViolations.stream().forEach { constraintViolation ->
            val property = (constraintViolation.propertyPath as PathImpl).leafNode.name
            body.add("$property ${constraintViolation.message}")
        }

        return HttpResponse
            .status<Errors>(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(Errors(body));
    }

}