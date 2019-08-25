package com.example.demo.domain.query.reprojection

import org.axonframework.config.EventProcessingConfiguration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import javax.annotation.PostConstruct

@Configuration
class ReprojectionConfiguration {
    @Autowired
    lateinit var eventProcessingConfiguration: EventProcessingConfiguration

    @Autowired
    lateinit var reprojectionMonitor: ReprojectionMonitor

    @PostConstruct
    fun configureEventProcessing() {
        //eventProcessingConfiguration.registerTrackingEventProcessor("reprojection-1")
        //eventProcessingConfiguration.registerTrackingEventProcessor("reprojection-2")
    }

    @EventListener
    fun on(event: ApplicationStartedEvent) {
        reprojectionMonitor.run()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ReprojectionConfiguration::class.java)
    }
}