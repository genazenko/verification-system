package com.sunfinance.group.verification.system.notification.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("notification.delay.config")
class NotificationDelayProperties {
    var delaySeconds: Long = 30
    var maxAttempts: Int = 5
}
