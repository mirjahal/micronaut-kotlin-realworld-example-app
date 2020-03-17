package io.realworld.app.micronaut.infrastructure.web.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

private const val DATE_TIME_FORMATTER_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

@Singleton
class ObjectMapperConfiguration : BeanCreatedEventListener<ObjectMapper> {

    override fun onCreated(event: BeanCreatedEvent<ObjectMapper>): ObjectMapper {
        val mapper = event.bean
        mapper.registerModule(javaTimeModule())
        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        return mapper
    }

    private fun javaTimeModule() = JavaTimeModule().addDeserializer(LocalDateTime::class.java, localDateTimeDeserializer())

    private fun localDateTimeDeserializer() = LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_PATTERN))

}