package com.sunfinance.group.verification.system.template.service

import com.sunfinance.group.verification.system.template.model.dto.RenderedTemplateDTO
import com.sunfinance.group.verification.system.template.model.dto.TemplateRequestDTO

interface TemplateService {
    fun getRenderedTemplate(templateRequestDTO: TemplateRequestDTO): RenderedTemplateDTO
}
