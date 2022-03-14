package com.sunfinance.group.verification.system.notification.builder

import com.sunfinance.group.verification.system.VerificationEventType.VerificationEvent
import com.sunfinance.group.verification.system.notification.exception.UnknownChannelTypeException
import com.sunfinance.group.verification.system.notification.model.constant.Channel
import com.sunfinance.group.verification.system.notification.model.constant.Status
import com.sunfinance.group.verification.system.notification.model.entity.Notification
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class NotificationBuilder {
    fun buildNew(verificationEvent: VerificationEvent): Notification {
        return Notification(
            recipient = verificationEvent.subject.identity.value,
            channel = identityTypeToChannel(verificationEvent.subject.type.value),
            status = Status.NEW,
            timeToProcess = LocalDateTime.now(),
            code = verificationEvent.code.value,
            eventId = verificationEvent.id.value
        )
    }

    private fun identityTypeToChannel(type: String): Channel {
        return Channel.getByIdentityType(type) ?: throw UnknownChannelTypeException()
    }
}
