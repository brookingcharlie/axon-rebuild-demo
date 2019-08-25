package com.example.demo.domain.query

import com.example.demo.domain.api.event.AccountOpened
import com.example.demo.domain.api.event.DepositMade
import com.example.demo.domain.api.event.WithdrawalMade
import com.example.demo.domain.api.query.AccountView
import com.example.demo.domain.api.query.AllAccountsQuery
import com.example.demo.domain.api.query.AllAccountsResponse
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("account-projector")
class AccountProjector(@Autowired private val repository: AccountViewRepository) {
    @EventHandler
    fun on(event: AccountOpened) {
        logger.debug("on(${event})")
        repository.save(AccountView().apply { accountNumber = event.accountNumber; balance = 0 })
    }

    @EventHandler
    fun on(event: DepositMade) {
        logger.debug("on(${event})")
        repository.getOne(event.accountNumber).apply { balance += event.amount }
        //repository.getOne(event.accountNumber).apply { numTransactions += 1 }
    }

    @EventHandler
    fun on(event: WithdrawalMade) {
        logger.debug("on(${event})")
        repository.getOne(event.accountNumber).apply { balance += event.amount }
        //repository.getOne(event.accountNumber).apply { balance -= event.amount }
        //repository.getOne(event.accountNumber).apply { numTransactions += 1 }
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