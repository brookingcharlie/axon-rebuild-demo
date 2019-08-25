package com.example.demo.domain.query

import org.axonframework.config.EventProcessingConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class ProjectionConfiguration {
    @Autowired
    lateinit var eventProcessingConfiguration: EventProcessingConfiguration

    @PostConstruct
    fun configureEventProcessing() {
        eventProcessingConfiguration.registerSubscribingEventProcessor("account-projector")
    }
}
