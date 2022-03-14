package com.sunfinance.group.verification.system.notification.client

import com.sunfinance.group.swagger.template.TemplateRequestDTO
import com.sunfinance.group.verification.system.notification.configuration.properties.TemplateProperties
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Component
class TemplateClient(
    private val templateRestTemplate: RestTemplate,
    private val templateProperties: TemplateProperties
) {
    fun renderTemplate(slugType: String, code: String): String? {
        try {
            val responseEntity = templateRestTemplate.postForEntity(
                templateProperties.endpoint,
                TemplateRequestDTO().apply {
                    slug = slugType
                    variables = mapOf("code" to code)
                },
                String::class.java
            )
            return responseEntity.body
        } catch (ex: Exception) {
            when (ex) {
                is HttpClientErrorException -> {
                    if (ex.statusCode == HttpStatus.NOT_FOUND) {
                        return null
                    }
                }
            }
            throw ex
        }
    }
}
