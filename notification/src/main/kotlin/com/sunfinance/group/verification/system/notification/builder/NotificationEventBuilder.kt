package com.sunfinance.group.verification.system.notification.builder

import com.google.protobuf.StringValue
import com.google.protobuf.Timestamp
import com.sunfinance.group.verification.system.NotificationEventType.NotificationEvent
import com.sunfinance.group.verification.system.notification.model.constant.Status
import com.sunfinance.group.verification.system.notification.model.entity.Notification
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class NotificationEventBuilder {
    fun buildNotificationEvent(notification: Notification): NotificationEvent {
        val currentTime = Instant.now()
        return NotificationEvent.newBuilder()
            .setId(StringValue.of(notification.id.toString()))
            .setOccurredOn(
                Timestamp.newBuilder()
                    .setSeconds(currentTime.epochSecond)
                    .setNanos(currentTime.nano)
                    .build()
            )
            .setEventType(statusEventTypeMap[notification.status])
            .build()
    }

    companion object {
        private val statusEventTypeMap =
            mapOf(
                Status.NEW to NotificationEvent.EventType.notificationCreated,
                Status.DISPATCHED to NotificationEvent.EventType.notificationDispatched
            )
    }
}
