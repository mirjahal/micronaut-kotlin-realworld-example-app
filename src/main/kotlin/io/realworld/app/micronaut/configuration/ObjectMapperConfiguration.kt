package io.realworld.app.micronaut.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import javax.inject.Singleton

@Singleton
class ObjectMapperConfiguration : BeanCreatedEventListener<ObjectMapper> {

    override fun onCreated(event: BeanCreatedEvent<ObjectMapper>): ObjectMapper {
        val mapper = event.bean
        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE)

        return mapper
    }

}