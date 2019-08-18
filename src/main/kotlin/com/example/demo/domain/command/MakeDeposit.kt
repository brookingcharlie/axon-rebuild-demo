package com.example.demo.domain.command

import org.axonframework.commandhandling.TargetAggregateIdentifier

data class MakeDeposit(@TargetAggregateIdentifier val accountNumber: String, val amount: Int)
