package com.example.demo.domain.command

import com.example.demo.domain.api.command.MakeDeposit
import com.example.demo.domain.api.command.MakeWithdrawal
import com.example.demo.domain.api.command.OpenAccount
import com.example.demo.domain.api.event.AccountOpened
import com.example.demo.domain.api.event.DepositMade
import com.example.demo.domain.api.event.WithdrawalMade
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import org.slf4j.LoggerFactory
import java.util.UUID

@Aggregate
class Account {
    @AggregateIdentifier
    lateinit var accountNumber: String

    var balance: Int = 0

    @CommandHandler
    constructor(command: OpenAccount) {
        logger.debug("[aggregate] constructor(${command})")
        AggregateLifecycle.apply(AccountOpened(UUID.randomUUID().toString()))
    }

    @CommandHandler
    fun handle(command: MakeDeposit) {
        AggregateLifecycle.apply(DepositMade(command.accountNumber, command.amount))
    }

    @CommandHandler
    fun handle(command: MakeWithdrawal) {
        AggregateLifecycle.apply(WithdrawalMade(command.accountNumber, command.amount))
    }

    constructor() {
        logger.debug("[aggregate] constructor()")
    }

    @EventSourcingHandler
    fun on(event: AccountOpened) {
        logger.debug("[aggregate] on(${event})")
        this.accountNumber = event.accountNumber
        this.balance = 0
    }

    @EventSourcingHandler
    fun on(event: DepositMade) {
        logger.debug("[aggregate] on(${event})")
        this.balance += event.amount
    }

    @EventSourcingHandler
    fun on(event: WithdrawalMade) {
        logger.debug("[aggregate] on(${event})")
        this.balance -= event.amount
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Account::class.java)
    }
}