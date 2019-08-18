package com.example.demo.subcriber

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "account")
class AccountView {
    @Id lateinit var accountNumber: String
    var balance: Int = 0
}