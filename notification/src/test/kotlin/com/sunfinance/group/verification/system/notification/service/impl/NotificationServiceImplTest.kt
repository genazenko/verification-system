package com.sunfinance.group.verification.system.notification.service.impl

import com.sunfinance.group.verification.system.notification.builder.NotificationBuilder
import com.sunfinance.group.verification.system.notification.client.TemplateClient
import com.sunfinance.group.verification.system.notification.configuration.properties.NotificationDelayProperties
import com.sunfinance.group.verification.system.notification.model.constant.Channel
import com.sunfinance.group.verification.system.notification.model.constant.Status
import com.sunfinance.group.verification.system.notification.model.entity.Notification
import com.sunfinance.group.verification.system.notification.repository.NotificationRepository
import com.sunfinance.group.verification.system.notification.service.DispatchContentService
import com.sunfinance.group.verification.system.notification.service.NotificationEventSender
import com.sunfinance.group.verification.system.notification.service.NotificationService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import java.time.LocalDateTime

internal class NotificationServiceImplTest {
    private val notificationRepository = mock(NotificationRepository::class.java)
    private val notificationBuilder = NotificationBuilder()
    private val templateClient = mock(TemplateClient::class.java)
    private val notificationDelayProperties = NotificationDelayProperties()
    private val emailDispatchContentService = mock(DispatchContentService::class.java)
    private val smsDispatchContentService = mock(DispatchContentService::class.java)
    private val dispatchContentServices = mapOf(
        Channel.email to emailDispatchContentService,
        Channel.phone to smsDispatchContentService
    )
    private val notificationEventSender = mock(NotificationEventSender::class.java)

    private val notificationService: NotificationService = NotificationServiceImpl(
        notificationRepository,
        notificationBuilder,
        templateClient,
        notificationDelayProperties,
        dispatchContentServices,
        notificationEventSender
    )

    @Test
    fun `render notifications`() {
        val first = Notification(
            1,
            "recipient1",
            Channel.phone,
            "code1",
            null,
            Status.NEW,
            LocalDateTime.now().minusSeconds(10),
            LocalDateTime.now().minusMinutes(1),
            0,
            "id1"
        )
        val second = Notification(
            2,
            "recipient2",
            Channel.email,
            "code2",
            null,
            Status.NEW,
            LocalDateTime.now().minusSeconds(10),
            LocalDateTime.now().minusMinutes(1),
            0,
            "id2"
        )
        val notifications = listOf(first, second)

        `when`(
            notificationRepository.findByStatusAndTimeToProcessBeforeOrderByTimeToProcessAsc(
                eq(Status.NEW),
                any(),
                any()
            )
        ).thenReturn(notifications)
        `when`(
            templateClient.renderTemplate(
                eq(first.channel.slugType),
                eq(first.code)
            )
        ).thenReturn("rendered template")
        `when`(
            templateClient.renderTemplate(
                eq(second.channel.slugType),
                eq(second.code)
            )
        ).thenReturn("rendered template")

        val count = notificationService.renderNotification(25)

        assert(count == 2)
        Mockito.verify(notificationRepository, times(1)).save(first)
        Mockito.verify(notificationRepository, times(1)).save(second)
    }

    @Test
    fun `dispatch notifications`() {
        val first = Notification(
            1,
            "recipient1",
            Channel.phone,
            "code1",
            "content",
            Status.RENDERED,
            LocalDateTime.now().minusSeconds(10),
            LocalDateTime.now().minusMinutes(1),
            0,
            "id1"
        )
        val second = Notification(
            2,
            "recipient2",
            Channel.phone,
            "code2",
            "content",
            Status.RENDERED,
            LocalDateTime.now().minusSeconds(10),
            LocalDateTime.now().minusMinutes(1),
            0,
            "id2"
        )
        val notifications = listOf(first, second)

        `when`(
            notificationRepository.findByStatusAndTimeToProcessBeforeOrderByTimeToProcessAsc(
                eq(Status.RENDERED),
                any(),
                any()
            )
        ).thenReturn(notifications)
        `when`(
            emailDispatchContentService.dispatchContent(
                eq(second.recipient),
                eq(second.content!!)
            )
        ).thenReturn(true)
        `when`(
            smsDispatchContentService.dispatchContent(
                eq(first.recipient),
                eq(first.content!!)
            )
        ).thenReturn(true)

        val count = notificationService.sendNotifications(25)

        assert(count == 2)
        Mockito.verify(notificationRepository, times(1)).save(first)
        Mockito.verify(notificationRepository, times(1)).save(second)
    }
}
