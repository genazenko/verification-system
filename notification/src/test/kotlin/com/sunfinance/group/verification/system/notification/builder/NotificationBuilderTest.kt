package com.sunfinance.group.verification.system.notification.builder

import com.google.protobuf.StringValue
import com.sunfinance.group.verification.system.SubjectType
import com.sunfinance.group.verification.system.VerificationEventType.VerificationEvent
import com.sunfinance.group.verification.system.notification.exception.UnknownChannelTypeException
import com.sunfinance.group.verification.system.notification.model.constant.Channel
import com.sunfinance.group.verification.system.notification.model.constant.Status
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class NotificationBuilderTest {
    private val notificationBuilder = NotificationBuilder()

    @Test
    fun `build new test`() {
        val verificationEvent = VerificationEvent
            .newBuilder()
            .setId(StringValue.of("id"))
            .setCode(StringValue.of("code"))
            .setSubject(
                SubjectType.Subject
                    .newBuilder()
                    .setIdentity(StringValue.of("+127368"))
                    .setType(StringValue.of(Channel.phone.identityType))
                    .build()
            )
            .build()
        val newEntity = notificationBuilder.buildNew(verificationEvent)
        assert(newEntity.eventId == verificationEvent.id.value)
        assert(newEntity.status == Status.NEW)
        assert(newEntity.recipient == verificationEvent.subject.identity.value)
        assert(newEntity.channel == Channel.getByIdentityType(verificationEvent.subject.type.value))
        assert(newEntity.code == verificationEvent.code.value)
    }

    @Test
    fun `unknown channel type exception`() {
        val verificationEvent = VerificationEvent
            .newBuilder()
            .setId(StringValue.of("id"))
            .setCode(StringValue.of("code"))
            .setSubject(
                SubjectType.Subject
                    .newBuilder()
                    .setIdentity(StringValue.of("+127368"))
                    .setType(StringValue.of("invalid type"))
                    .build()
            )
            .build()
        Assertions.assertThrows(UnknownChannelTypeException::class.java) {
            notificationBuilder.buildNew(
                verificationEvent
            )
        }
    }
}
