package com.example.demo.domain.query

import com.example.demo.domain.api.event.AccountOpened
import com.example.demo.domain.api.event.DepositMade
import com.example.demo.domain.api.event.WithdrawalMade
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("tracker")
class AccountProjector2 {
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
        private val logger = LoggerFactory.getLogger(AccountProjector2::class.java)
    }
}