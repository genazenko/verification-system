package com.sunfinance.group.verification.system.verification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class VerificationApplication

fun main(args: Array<String>) {
    runApplication<VerificationApplication>(*args)
}
