package com.example.demo.tracker

import com.example.demo.domain.event.AccountOpened
import com.example.demo.domain.event.DepositMade
import com.example.demo.domain.event.WithdrawalMade
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("tracker")
class TrackingProjector {
    @EventHandler
    fun on(event: AccountOpened) {
        logger.debug("[tracker] on(${event})")
    }

    @EventHandler
    fun on(event: DepositMade) {
        logger.debug("[tracker] on(${event})")
    }

    @EventHandler
    fun on(event: WithdrawalMade) {
        logger.debug("[tracker] on(${event})")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TrackingProjector::class.java)
    }
}