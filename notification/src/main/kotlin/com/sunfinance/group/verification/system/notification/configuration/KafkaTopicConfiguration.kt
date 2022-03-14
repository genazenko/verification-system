package com.sunfinance.group.verification.system.notification.configuration

import com.sunfinance.group.verification.system.notification.configuration.properties.KafkaProducerProperties
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicConfiguration {

    @Bean
    fun verificationEventTopic(kafkaProducerProperties: KafkaProducerProperties): NewTopic {
        return TopicBuilder.name(kafkaProducerProperties.topicName).build()
    }
}
