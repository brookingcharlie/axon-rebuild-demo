package com.example.demo.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigInteger
import javax.persistence.EntityManager

@Service
class GlobalIndexService(@Autowired val entityManager: EntityManager) {
    fun get(): Long {
        val query = entityManager.createNativeQuery("select currval('hibernate_sequence')")
        return (query.singleResult as BigInteger).toLong()
    }
}