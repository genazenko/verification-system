package com.sunfinance.group.verification.system.template.service.impl

import com.sunfinance.group.verification.system.template.exception.TemplateNotFoundException
import com.sunfinance.group.verification.system.template.model.PARAM_TEMPLATE
import com.sunfinance.group.verification.system.template.model.dto.RenderedTemplateDTO
import com.sunfinance.group.verification.system.template.model.dto.TemplateRequestDTO
import com.sunfinance.group.verification.system.template.repository.TemplateRepository
import com.sunfinance.group.verification.system.template.service.TemplateService
import com.sunfinance.group.verification.system.template.util.TemplateRequestValidator
import org.springframework.stereotype.Service

@Service
class TemplateServiceImpl(
    private val templateRepository: TemplateRepository,
    private val templateRequestValidator: TemplateRequestValidator
) : TemplateService {
    override fun getRenderedTemplate(templateRequestDTO: TemplateRequestDTO): RenderedTemplateDTO {
        templateRequestValidator.validateRequest(templateRequestDTO)
        return templateRepository
            .findOneBySlug(templateRequestDTO.slug!!)
            .map {
                RenderedTemplateDTO(
                    content = renderTemplate(it.content, templateRequestDTO.variables),
                    contentType = it.contentType
                )
            }
            .orElseThrow { TemplateNotFoundException("Template with slug = ${templateRequestDTO.slug} is not found") }
    }

    private fun renderTemplate(content: String, variables: Map<String, String>?): String {
        var result = content
        variables?.forEach {
            result = replaceOneParam(result, PARAM_TEMPLATE.format(it.key), it.value)
        }
        return result
    }

    private fun replaceOneParam(content: String, param: String, value: String): String =
        content.replace(param, value)
}
