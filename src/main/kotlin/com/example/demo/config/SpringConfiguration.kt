package com.example.demo.config

import com.example.demo.service.ReprojectionMonitor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class SpringConfiguration {
    @Autowired
    lateinit var reprojectionMonitor: ReprojectionMonitor

    @EventListener
    fun on(event: ApplicationStartedEvent) {
        reprojectionMonitor.run()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SpringConfiguration::class.java)
    }
}