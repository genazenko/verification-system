package com.sunfinance.group.verification.system.notification.service.impl

import com.sunfinance.group.verification.system.VerificationEventType
import com.sunfinance.group.verification.system.notification.builder.NotificationBuilder
import com.sunfinance.group.verification.system.notification.client.TemplateClient
import com.sunfinance.group.verification.system.notification.configuration.properties.NotificationDelayProperties
import com.sunfinance.group.verification.system.notification.model.constant.Channel
import com.sunfinance.group.verification.system.notification.model.constant.Status
import com.sunfinance.group.verification.system.notification.repository.NotificationRepository
import com.sunfinance.group.verification.system.notification.service.DispatchContentService
import com.sunfinance.group.verification.system.notification.service.NotificationEventSender
import com.sunfinance.group.verification.system.notification.service.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository,
    private val notificationBuilder: NotificationBuilder,
    private val templateClient: TemplateClient,
    private val notificationDelayProperties: NotificationDelayProperties,
    private val dispatchContentServices: Map<Channel, DispatchContentService>,
    private val notificationEventSender: NotificationEventSender
) : NotificationService {
    override fun createNewNotification(verificationEvent: VerificationEventType.VerificationEvent): Long {
        val notification = notificationBuilder.buildNew(verificationEvent)
        notificationRepository.save(notification)
        notificationEventSender.sendNotificationEvent(notification)
        return notification.id!!
    }

    @Transactional
    override fun renderNotification(batchSize: Int): Int {
        val notifications =
            notificationRepository.findByStatusAndTimeToProcessBeforeOrderByTimeToProcessAsc(
                Status.NEW,
                LocalDateTime.now(),
                PageRequest.of(0, batchSize)
            )

        return notifications?.map {
            var isError = false
            try {
                logger.info("Try to render notification: channel = ${it.channel}")
                it.content = templateClient.renderTemplate(it.channel.slugType, it.code)
                logger.info("Notification is rendered: channel = ${it.channel}, content = ${it.content}")
                it.status = Status.RENDERED
                it.timeToProcess = LocalDateTime.now()
                it.attemptCount = 0
            } catch (ex: Exception) {
                it.attemptCount += 1
                it.timeToProcess =
                    LocalDateTime.now().plusSeconds(notificationDelayProperties.delaySeconds)
                if (it.attemptCount >= notificationDelayProperties.maxAttempts) {
                    it.status = Status.RENDER_FAILED
                }
                isError = true
            } finally {
                notificationRepository.save(it)
            }
            isError
        }
            ?.count { !it } ?: 0
    }

    @Transactional
    override fun sendNotifications(batchSize: Int): Int {
        val notifications =
            notificationRepository.findByStatusAndTimeToProcessBeforeOrderByTimeToProcessAsc(
                Status.RENDERED,
                LocalDateTime.now(),
                PageRequest.of(0, batchSize)
            )
        return notifications
            ?.filter { it.content != null }
            ?.map {
                var isError = false
                try {
                    logger.info("Try to dispatch notification: channel = ${it.channel}, content = ${it.content}")
                    dispatchContentServices[it.channel]?.dispatchContent(it.recipient, it.content!!)
                    it.status = Status.DISPATCHED
                    it.timeToProcess = LocalDateTime.now()
                    it.attemptCount = 0
                } catch (ex: Exception) {
                    logger.error("Dispatching notification fails due to error: ", ex)
                    it.attemptCount += 1
                    it.timeToProcess =
                        LocalDateTime.now().plusSeconds(notificationDelayProperties.delaySeconds)
                    if (it.attemptCount >= notificationDelayProperties.maxAttempts) {
                        it.status = Status.DISPATCH_FAILED
                    }
                    isError = true
                } finally {
                    notificationRepository.save(it)
                    if (!isError) {
                        notificationEventSender.sendNotificationEvent(it)
                    }
                }
                isError
            }
            ?.count { !it } ?: 0
    }

    companion object {
        private val logger = LoggerFactory.getLogger(NotificationService::class.java)
    }
}
