package com.sunfinance.group.verification.system.notification.service.impl

import com.sunfinance.group.verification.system.notification.model.constant.Channel
import com.sunfinance.group.verification.system.notification.service.DispatchContentService
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service


@Service
class EmailDispatchContentService(
    private val emailSender: JavaMailSender
) : DispatchContentService {
    override fun getChannel(): Channel = CHANNEL

    override fun dispatchContent(recipient: String, content: String): Boolean {
        val message = emailSender.createMimeMessage()
        val messageHelper = MimeMessageHelper(message)
        message.setContent(content, HTML_MESSAGE_TYPE)
        messageHelper.setFrom(FROM)
        messageHelper.setTo(recipient)
        messageHelper.setSubject(SUBJECT_MESSAGE)
        emailSender.send(message)
        return true
    }

    companion object {
        private val CHANNEL = Channel.email
        private const val FROM = "noreply@sunfinance.com"
        private const val SUBJECT_MESSAGE = "Your confirmation code"
        private const val HTML_MESSAGE_TYPE = "text/html"
    }
}
