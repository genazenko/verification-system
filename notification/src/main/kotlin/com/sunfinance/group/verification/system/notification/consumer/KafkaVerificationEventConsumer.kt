package com.sunfinance.group.verification.system.notification.consumer

import com.google.protobuf.InvalidProtocolBufferException
import com.sunfinance.group.verification.system.VerificationEventType
import com.sunfinance.group.verification.system.notification.service.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class KafkaVerificationEventConsumer(
    private val notificationService: NotificationService
) {

    @KafkaListener(topics = ["\${kafka.consumer.verification.topicName}"])
    fun processVerificationEvent(
        @Payload message: ByteArray,
        acknowledgment: Acknowledgment
    ) {
        try {
            val event = parseVerificationEvent(message)
            if (event.eventType == VerificationEventType.VerificationEvent.EventType.verificationCreated) {
                notificationService.createNewNotification(event)
            }
        } catch (ex: Exception) {
            logger.error("Message processing failed due to error: ", ex)
        } finally {
            acknowledgment.acknowledge()
        }
    }

    private fun parseVerificationEvent(message: ByteArray): VerificationEventType.VerificationEvent {
        try {
            return VerificationEventType.VerificationEvent.parseFrom(message)
        } catch (ex: InvalidProtocolBufferException) {
            logger.error("Invalid message")
            throw ex
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(KafkaVerificationEventConsumer::class.java)
    }
}
