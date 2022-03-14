package com.sunfinance.group.verification.system.template.util

import com.sunfinance.group.verification.system.template.exception.TemplateValidationException
import com.sunfinance.group.verification.system.template.model.dto.TemplateRequestDTO
import org.springframework.stereotype.Component

@Component
class TemplateRequestValidator {
    fun validateRequest(templateRequestDTO: TemplateRequestDTO) {
        if (templateRequestDTO.slug.isNullOrBlank()) {
            throw TemplateValidationException(VALIDATION_ERROR_MESSAGE)
        }
    }

    companion object {
        private const val VALIDATION_ERROR_MESSAGE = "Slug must not be null or blank"
    }
}
