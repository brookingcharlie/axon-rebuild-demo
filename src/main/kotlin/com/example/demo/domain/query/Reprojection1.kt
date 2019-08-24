package com.example.demo.domain.query

import com.example.demo.domain.api.event.AccountOpened
import com.example.demo.domain.api.event.DepositMade
import com.example.demo.domain.api.event.WithdrawalMade
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("reprojection-1")
class Reprojection1 {
    @EventHandler
    fun on(event: AccountOpened) {
        logger.debug("[reprojection-1] on(${event})")
    }

    @EventHandler
    fun on(event: DepositMade) {
        logger.debug("[reprojection-1] on(${event})")
    }

    @EventHandler
    fun on(event: WithdrawalMade) {
        logger.debug("[reprojection-1] on(${event})")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Reprojection1::class.java)
    }
}