package com.example.demo.domain.api.query

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "account")
class AccountView {
    @Id
    lateinit var accountNumber: String

    var balance: Int = 0

    //var numTransactions: Int = 0
}