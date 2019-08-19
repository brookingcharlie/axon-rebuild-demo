package com.example.demo.api

import com.example.demo.domain.api.command.MakeDeposit
import com.example.demo.domain.api.command.MakeWithdrawal
import com.example.demo.domain.api.query.AccountView
import com.example.demo.domain.api.query.AllAccountsQuery
import com.example.demo.domain.api.query.AllAccountsResponse
import com.example.demo.service.DataLoader
import com.example.demo.service.TrackerManager
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
        @Autowired val queryGateway: QueryGateway,
        @Autowired val trackerManager: TrackerManager
) {
    @PostMapping("/load")
    fun load() {
        logger.info("[controller] load()")
        dataLoader.createAccounts()
    }

    @GetMapping("/account")
    fun getAllAccounts(): List<AccountView> {
        return queryGateway.query(AllAccountsQuery(), AllAccountsResponse::class.java).get().accounts
    }

    @PostMapping("/account/{accountNumber}/deposit")
    fun makeDeposit(@PathVariable("accountNumber") accountNumber: String) {
        commandGateway.send<Void>(MakeDeposit(accountNumber, Random().nextInt()))
    }

    @PostMapping("/account/{accountNumber}/withdraw")
    fun makeWithdrawal(@PathVariable("accountNumber") accountNumber: String) {
        commandGateway.send<Void>(MakeWithdrawal(accountNumber, Random().nextInt()))
    }

    @PostMapping("/tracker/shut-down")
    fun shutDown() {
        logger.info("[controller] shutDown()")
        trackerManager.shutDown()
    }

    @PostMapping("/tracker/start")
    fun start() {
        logger.info("[controller] start()")
        trackerManager.start()
    }

    @PostMapping("/tracker/rebuild")
    fun rebuild() {
        logger.info("[controller] rebuild()")
        trackerManager.rebuild()
    }

    @GetMapping("/tracker/status")
    fun status(): TrackerManager.Status {
        logger.info("[controller] status()")
        return trackerManager.status()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Controller::class.java)
    }
}