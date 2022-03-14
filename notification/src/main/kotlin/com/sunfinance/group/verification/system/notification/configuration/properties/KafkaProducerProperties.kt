package com.sunfinance.group.verification.system.notification.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("kafka.producer.notification")
class KafkaProducerProperties {
    var topicName: String = "notification.event"
}
