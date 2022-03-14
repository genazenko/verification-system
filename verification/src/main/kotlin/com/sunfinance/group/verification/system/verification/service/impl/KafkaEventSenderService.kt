package com.sunfinance.group.verification.system.verification.service.impl

import com.sunfinance.group.verification.system.VerificationEventType.VerificationEvent
import com.sunfinance.group.verification.system.verification.builder.VerificationEventBuilder
import com.sunfinance.group.verification.system.verification.configuration.properties.KafkaProducerProperties
import com.sunfinance.group.verification.system.verification.model.entity.Verification
import com.sunfinance.group.verification.system.verification.service.EventSenderService
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaEventSenderService(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val verificationEventBuilder: VerificationEventBuilder,
    private val kafkaProducerProperties: KafkaProducerProperties
) : EventSenderService {
    override fun sendVerificationCreatedEvent(verification: Verification) {
        val event = verificationEventBuilder.buildVerificationEvent(
            verification,
            VerificationEvent.EventType.verificationCreated
        )
        sendEvent(verification.id, event)
    }

    override fun sendVerificationConfirmedEvent(verification: Verification) {
        wrapException {
            val event = verificationEventBuilder.buildVerificationEvent(
                verification,
                VerificationEvent.EventType.verificationConfirmed
            )
            sendEvent(verification.id, event)
        }
    }

    override fun sendVerificationConfirmationFailedEvent(verification: Verification) {
        wrapException {
            val event = verificationEventBuilder.buildVerificationEvent(
                verification,
                VerificationEvent.EventType.verificationFailed
            )
            sendEvent(verification.id, event)
        }
    }

    private fun sendEvent(id: String, verificationEvent: VerificationEvent) {
        logger.info("Sending verificationEvent = {}", verificationEvent.toString())
        val record = ProducerRecord(
            kafkaProducerProperties.topicName,
            null,
            id,
            verificationEvent.toByteArray()
        )
        kafkaTemplate.send(record)
    }

    private fun <T> wrapException(block: () -> T) {
        try {
            block()
        } catch (ex: Exception) {
            logger.error(LOG_ERROR_MESSAGE, ex)
        }
    }

    companion object {
        private const val LOG_ERROR_MESSAGE = "Error on sending verification event: "
        private val logger = LoggerFactory.getLogger(KafkaEventSenderService::class.java)
    }
}
