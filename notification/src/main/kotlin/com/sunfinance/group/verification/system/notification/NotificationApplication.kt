package com.sunfinance.group.verification.system.notification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
@EnableAsync
class NotificationApplication

fun main(args: Array<String>) {
    runApplication<NotificationApplication>(*args)
}
