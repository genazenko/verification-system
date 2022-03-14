package com.sunfinance.group.verification.system.template.service.impl

import com.sunfinance.group.verification.system.template.exception.TemplateNotFoundException
import com.sunfinance.group.verification.system.template.exception.TemplateValidationException
import com.sunfinance.group.verification.system.template.model.dto.TemplateRequestDTO
import com.sunfinance.group.verification.system.template.model.entity.Template
import com.sunfinance.group.verification.system.template.repository.TemplateRepository
import com.sunfinance.group.verification.system.template.service.TemplateService
import com.sunfinance.group.verification.system.template.util.TemplateRequestValidator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.Optional

internal class TemplateServiceImplTest {
    private val templateRepository = mock(TemplateRepository::class.java)
    private val templateRequestValidator = TemplateRequestValidator()
    private val templateService: TemplateService =
        TemplateServiceImpl(templateRepository, templateRequestValidator)

    @Test
    fun `get rendered template`() {
        val templateRequestDTO = TemplateRequestDTO("email-confirmation", emptyMap())
        val template = Template(
            1,
            templateRequestDTO.slug!!,
            "any template",
            "text/plain"
        )
        `when`(templateRepository.findOneBySlug(templateRequestDTO.slug!!))
            .thenReturn(Optional.of(template))
        val renderedTemplate = templateService.getRenderedTemplate(templateRequestDTO)
        assert(renderedTemplate.content == template.content)
        assert(renderedTemplate.contentType == template.contentType)
    }

    @Test
    fun `get rendered template with params`() {
        val templateRequestDTO =
            TemplateRequestDTO("email-confirmation", mapOf("param" to "render"))
        val template = Template(
            1,
            templateRequestDTO.slug!!,
            "any template with param {{ param }}",
            "text/plain"
        )
        `when`(templateRepository.findOneBySlug(templateRequestDTO.slug!!))
            .thenReturn(Optional.of(template))
        val renderedTemplate = templateService.getRenderedTemplate(templateRequestDTO)
        assert(renderedTemplate.content == "any template with param render")
        assert(renderedTemplate.contentType == template.contentType)
    }

    @Test
    fun `request validation exception`() {
        val templateRequestDTO =
            TemplateRequestDTO("", mapOf("param" to "render"))
        val thrown = Assertions.assertThrows(TemplateValidationException::class.java) {
            templateService.getRenderedTemplate(templateRequestDTO)
        }
        assert(thrown.message == "Slug must not be null or blank")
    }

    @Test
    fun `template not found exception`() {
        val templateRequestDTO =
            TemplateRequestDTO("asd", mapOf("param" to "render"))
        `when`(templateRepository.findOneBySlug(templateRequestDTO.slug!!))
            .thenReturn(Optional.empty())
        val thrown = Assertions.assertThrows(TemplateNotFoundException::class.java) {
            templateService.getRenderedTemplate(templateRequestDTO)
        }
        assert(thrown.message == "Template with slug = ${templateRequestDTO.slug} is not found")
    }
}
