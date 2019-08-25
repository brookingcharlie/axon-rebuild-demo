package com.example.demo.domain.query.reprojection

import com.example.demo.domain.api.event.DepositMade
import com.example.demo.domain.api.event.WithdrawalMade
import com.example.demo.domain.query.AccountViewRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

//@Component
//@ProcessingGroup("reprojection-2")
//class Reprojection2(@Autowired private val repository: AccountViewRepository) {
//    @EventHandler
//    fun on(event: DepositMade) {
//        repository.getOne(event.accountNumber).apply { numTransactions += 1 }
//    }
//
//    @EventHandler
//    fun on(event: WithdrawalMade) {
//        repository.getOne(event.accountNumber).apply { numTransactions += 1 }
//    }
//
//    companion object {
//        private val logger = LoggerFactory.getLogger(Reprojection2::class.java)
//    }
//}