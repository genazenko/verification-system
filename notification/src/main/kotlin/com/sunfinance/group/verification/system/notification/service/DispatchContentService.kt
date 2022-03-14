package com.sunfinance.group.verification.system.notification.service

import com.sunfinance.group.verification.system.notification.model.constant.Channel

interface DispatchContentService {
    fun getChannel(): Channel
    fun dispatchContent(recipient: String, content: String): Boolean
}
