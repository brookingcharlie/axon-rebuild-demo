package com.example.demo.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.axonframework.eventhandling.ListenerInvocationErrorHandler
import org.axonframework.eventhandling.PropagatingErrorHandler
import org.axonframework.serialization.json.JacksonSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class AxonConfiguration {
    @Bean
    @Primary
    fun serializer(objectMapper: ObjectMapper) = JacksonSerializer(objectMapper)

    @Bean
    fun errorHandler(): ListenerInvocationErrorHandler = PropagatingErrorHandler.INSTANCE
}
