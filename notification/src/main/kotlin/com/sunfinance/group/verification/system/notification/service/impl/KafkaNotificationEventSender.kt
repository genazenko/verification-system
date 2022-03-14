package com.sunfinance.group.verification.system.notification.service.impl

import com.sunfinance.group.verification.system.NotificationEventType.NotificationEvent
import com.sunfinance.group.verification.system.notification.builder.NotificationEventBuilder
import com.sunfinance.group.verification.system.notification.configuration.properties.KafkaProducerProperties
import com.sunfinance.group.verification.system.notification.model.entity.Notification
import com.sunfinance.group.verification.system.notification.service.NotificationEventSender
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaNotificationEventSender(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val notificationEventBuilder: NotificationEventBuilder,
    private val kafkaProducerProperties: KafkaProducerProperties
) : NotificationEventSender {

    override fun sendNotificationEvent(notification: Notification) {
        val event = notificationEventBuilder.buildNotificationEvent(notification)
        sendEvent(notification.id.toString(), event)
    }

    private fun sendEvent(id: String, notificationEvent: NotificationEvent) {
        try {
            logger.info("Sending notificationEvent = {}", notificationEvent.toString())
            val record = ProducerRecord(
                kafkaProducerProperties.topicName,
                null,
                id,
                notificationEvent.toByteArray()
            )
            kafkaTemplate.send(record)
        } catch (ex: Exception) {
            logger.error("Error on sending notification event: ", ex)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(KafkaNotificationEventSender::class.java)
    }
}
