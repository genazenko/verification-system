package com.sunfinance.group.verification.system.verification.builder

import com.google.protobuf.StringValue
import com.google.protobuf.Timestamp
import com.sunfinance.group.verification.system.SubjectType.Subject
import com.sunfinance.group.verification.system.VerificationEventType.VerificationEvent
import com.sunfinance.group.verification.system.VerificationEventType.VerificationEvent.EventType
import com.sunfinance.group.verification.system.verification.model.entity.Verification
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class VerificationEventBuilder {
    fun buildVerificationEvent(
        verification: Verification,
        eventType: EventType
    ): VerificationEvent {
        val currentTime = Instant.now()
        return VerificationEvent.newBuilder()
            .setEventType(eventType)
            .setCode(StringValue.of(verification.code))
            .setId(StringValue.of(verification.id))
            .setOccurredOn(
                Timestamp.newBuilder()
                    .setSeconds(currentTime.epochSecond)
                    .setNanos(currentTime.nano)
                    .build()
            )
            .setSubject(
                Subject.newBuilder()
                    .setIdentity(StringValue.of(verification.identity))
                    .setType(StringValue.of(verification.type.name))
            )
            .build()
    }
}
