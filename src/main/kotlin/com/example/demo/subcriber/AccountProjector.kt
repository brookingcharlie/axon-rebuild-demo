package com.example.demo.subcriber

import com.example.demo.domain.event.AccountOpened
import com.example.demo.domain.event.DepositMade
import com.example.demo.domain.event.WithdrawalMade
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("subscriber")
class AccountProjector(@Autowired private val repository: AccountViewRepository) {
    @EventHandler
    fun on(event: AccountOpened) {
        logger.debug("[subscriber] on(${event})")
        repository.save(AccountView().apply { accountNumber = event.accountNumber; balance = 0 })
    }

    @EventHandler
    fun on(event: DepositMade) {
        logger.debug("[subscriber] on(${event})")
        repository.getOne(event.accountNumber).apply { balance += event.amount }
    }

    @EventHandler
    fun on(event: WithdrawalMade) {
        logger.debug("[subscriber] on(${event})")
        repository.getOne(event.accountNumber).apply { balance -= event.amount }
    }

    @QueryHandler
    fun on(query: AllAccountsQuery): AllAccountsResponse {
        val accounts = repository.findAll()
        return AllAccountsResponse(accounts)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AccountProjector::class.java)
    }
}