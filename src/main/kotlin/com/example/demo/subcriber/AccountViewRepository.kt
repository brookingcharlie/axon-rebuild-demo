package com.example.demo.subcriber

import org.springframework.data.jpa.repository.JpaRepository

interface AccountViewRepository: JpaRepository<AccountView, String>
