package com.sunfinance.group.verification.system.notification.service

import com.sunfinance.group.verification.system.VerificationEventType.VerificationEvent

interface NotificationService {
    fun createNewNotification(verificationEvent: VerificationEvent): Long
    fun renderNotification(batchSize: Int): Int
    fun sendNotifications(batchSize: Int): Int
}
