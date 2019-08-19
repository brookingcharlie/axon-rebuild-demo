package com.example.demo.domain.api.command

import org.axonframework.commandhandling.TargetAggregateIdentifier

data class MakeDeposit(@TargetAggregateIdentifier val accountNumber: String, val amount: Int)
