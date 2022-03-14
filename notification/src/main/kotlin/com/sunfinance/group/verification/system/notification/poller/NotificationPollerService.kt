package com.sunfinance.group.verification.system.notification.poller

import com.sunfinance.group.verification.system.notification.configuration.properties.NewPollerProperties
import com.sunfinance.group.verification.system.notification.configuration.properties.RenderedPollerProperties
import com.sunfinance.group.verification.system.notification.service.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class NotificationPollerService(
    private val notificationService: NotificationService,
    private val newPollerProperties: NewPollerProperties,
    private val renderedPollerProperties: RenderedPollerProperties
) {

    @Async("NEW_NOTIFICATION_POOL")
    @Scheduled(fixedRateString = "\${poller.new.delay}")
    fun processNewNotifications() {
        val renderedNotifications =
            notificationService.renderNotification(newPollerProperties.batchSize)
        logger.info("$renderedNotifications notifications are rendered")
    }

    @Async("RENDERED_NOTIFICATION_POOL")
    @Scheduled(fixedRateString = "\${poller.rendered.delay}")
    fun processRenderedNotifications() {
        val dispatchedNotifications =
            notificationService.sendNotifications(renderedPollerProperties.batchSize)
        logger.info("$dispatchedNotifications notifications are dispatched")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(NotificationPollerService::class.java)
    }
}
