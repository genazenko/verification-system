package com.sunfinance.group.verification.system.notification.service.impl

import com.sunfinance.group.verification.system.notification.client.GotifyClient
import com.sunfinance.group.verification.system.notification.model.constant.Channel
import com.sunfinance.group.verification.system.notification.service.DispatchContentService
import org.springframework.stereotype.Service

@Service
class SmsDispatchContentService(
    private val gotifyClient: GotifyClient
) : DispatchContentService {
    override fun getChannel(): Channel = CHANNEL

    override fun dispatchContent(recipient: String, content: String): Boolean {
        gotifyClient.sendMessage(content, recipient)
        return true
    }

    companion object {
        private val CHANNEL = Channel.phone
    }
}
