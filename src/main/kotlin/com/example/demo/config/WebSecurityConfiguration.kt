package com.example.demo.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@Configuration
@EnableWebSecurity
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var maintenanceRequestMatcher: MaintenanceRequestMatcher

    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .requestMatchers(maintenanceRequestMatcher).denyAll()
    }
}