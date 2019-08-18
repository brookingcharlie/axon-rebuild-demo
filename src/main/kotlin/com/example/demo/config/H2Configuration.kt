package com.example.demo.config

import org.h2.tools.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.SQLException

@Configuration
class H2Configuration {
    @Bean(initMethod = "start", destroyMethod = "stop")
    @Throws(SQLException::class)
    fun inMemoryDatabaseServer(): Server {
        return Server.createTcpServer()
    }
}
