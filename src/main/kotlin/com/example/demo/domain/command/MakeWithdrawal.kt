package com.example.demo.domain.command

import org.axonframework.commandhandling.TargetAggregateIdentifier

data class MakeWithdrawal(@TargetAggregateIdentifier val accountNumber: String, val amount: Int)
