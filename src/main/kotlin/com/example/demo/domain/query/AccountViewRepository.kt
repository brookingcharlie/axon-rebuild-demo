package com.example.demo.domain.query

import com.example.demo.domain.api.query.AccountView
import org.springframework.data.jpa.repository.JpaRepository

interface AccountViewRepository : JpaRepository<AccountView, String>
