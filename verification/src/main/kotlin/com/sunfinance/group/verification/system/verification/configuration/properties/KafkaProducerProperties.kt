package com.sunfinance.group.verification.system.verification.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("kafka.producer.verification")
class KafkaProducerProperties {
    var topicName: String = "verification.event"
}
