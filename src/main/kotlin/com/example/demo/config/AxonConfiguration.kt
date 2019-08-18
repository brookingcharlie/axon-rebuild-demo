package com.example.demo.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.eventhandling.ListenerInvocationErrorHandler
import org.axonframework.eventhandling.PropagatingErrorHandler
import org.axonframework.serialization.json.JacksonSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.annotation.PostConstruct

@Configuration
class AxonConfiguration {
    @Autowired
    lateinit var eventProcessingConfiguration: EventProcessingConfiguration

    @Bean
    @Primary
    fun serializer(objectMapper: ObjectMapper) = JacksonSerializer(objectMapper)

    @Bean
    fun errorHandler(): ListenerInvocationErrorHandler = PropagatingErrorHandler.INSTANCE

    @PostConstruct
    fun configureEventProcessing() {
        eventProcessingConfiguration.registerSubscribingEventProcessor("subscriber")
        eventProcessingConfiguration.registerTrackingEventProcessor("tracker")
    }
}
