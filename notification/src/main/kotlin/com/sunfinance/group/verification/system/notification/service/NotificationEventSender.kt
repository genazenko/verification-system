package com.sunfinance.group.verification.system.notification.service

import com.sunfinance.group.verification.system.notification.model.entity.Notification

interface NotificationEventSender {
    fun sendNotificationEvent(notification: Notification)
}
