package com.example.demo.api

import com.example.demo.domain.api.command.MakeDeposit
import com.example.demo.domain.api.command.MakeWithdrawal
import com.example.demo.domain.api.query.AccountView
import com.example.demo.domain.api.query.AllAccountsQuery
import com.example.demo.domain.api.query.AllAccountsResponse
import com.example.demo.service.DataLoader
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Random

@RestController
class Controller(
        @Autowired val dataLoader: DataLoader,
        @Autowired val commandGateway: CommandGateway,
        @Autowired val queryGateway: QueryGateway
) {
    @PostMapping("/load")
    fun load() {
        logger.info("load()")
        dataLoader.createAccounts()
    }

    @GetMapping("/account")
    fun getAllAccounts(): List<AccountView> {
        return queryGateway.query(AllAccountsQuery(), AllAccountsResponse::class.java).get().accounts
    }

    @PostMapping("/account/{accountNumber}/deposit")
    fun makeDeposit(@PathVariable("accountNumber") accountNumber: String) {
        commandGateway.send<Void>(MakeDeposit(accountNumber, Random().nextInt(100)))
    }

    @PostMapping("/account/{accountNumber}/withdraw")
    fun makeWithdrawal(@PathVariable("accountNumber") accountNumber: String) {
        commandGateway.send<Void>(MakeWithdrawal(accountNumber, Random().nextInt(100)))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Controller::class.java)
    }
}