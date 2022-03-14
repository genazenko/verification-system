package com.sunfinance.group.verification.system.notification.client

import com.sunfinance.group.swagger.gotify.Message
import com.sunfinance.group.verification.system.notification.configuration.properties.GotifyProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class GotifyClient(
    private val gotifyRestTemplate: RestTemplate,
    private val gotifyProperties: GotifyProperties
) {
    fun sendMessage(content: String, recipient: String): Message? {
        try {
            val responseEntity = gotifyRestTemplate.postForEntity(
                gotifyProperties.endpoint,
                Message().apply {
                    message = content
                    extras = mapOf(RECIPIENT_PRONE_KEY to recipient)
                    title = VERIFICATION_CODE_TITLE
                },
                Message::class.java,
                mapOf(TOKEN_PARAM_KEY to gotifyProperties.token)
            )
            return responseEntity.body
        } catch (ex: Exception) {
            logger.error("Fail on sending message = {} to client = {}: ", content, recipient, ex)
            throw ex
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GotifyClient::class.java)
        private const val VERIFICATION_CODE_TITLE = "Confirmation code"
        private const val RECIPIENT_PRONE_KEY = "recipient::phone"
        private const val TOKEN_PARAM_KEY = "token"
    }
}
