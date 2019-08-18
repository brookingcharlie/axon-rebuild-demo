package com.example.demo.service

import com.example.demo.domain.command.OpenAccount
import com.example.demo.domain.command.MakeDeposit
import com.example.demo.domain.command.MakeWithdrawal
import org.axonframework.commandhandling.gateway.CommandGateway
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.Random
import java.util.UUID
import java.util.concurrent.CompletableFuture
import kotlin.system.measureTimeMillis

@Service
class DataLoader(
        @Autowired val commandGateway: CommandGateway,
        @Value("\${com.example.demo.numAccounts}") val numAccounts: Int,
        @Value("\${com.example.demo.numTransactions}") val numTransactions: Int
) {
    val random = Random()

    @Async
    fun createAccounts() {
        logger.debug("[loader] createAccounts()")
        val time = measureTimeMillis {
            val createAccountFutures = (1..numAccounts).map { createAccount() }
            CompletableFuture.allOf(*createAccountFutures.toTypedArray())
        }
        logger.info("[loader] Loaded ${numAccounts} records in ${time} ms")
    }

    private fun createAccount(): CompletableFuture<Void> {
        logger.debug("[loader] createAccount()")
        return commandGateway
                .send<String>(OpenAccount())
                .thenAccept { accountNumber -> createTransactions(accountNumber) }
    }

    private fun createTransactions(accountNumber: String): CompletableFuture<Void> {
        val createTransactionsFutures = (1..numTransactions).map {
            val command = when (random.nextBoolean()) {
                false -> MakeWithdrawal(accountNumber, random.nextInt())
                true -> MakeDeposit(accountNumber, random.nextInt())
            }
            commandGateway.send<Void>(command)
        }
        return CompletableFuture.allOf(*createTransactionsFutures.toTypedArray())
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DataLoader::class.java)
    }
}