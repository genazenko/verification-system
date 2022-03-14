package com.sunfinance.group.verification.system.template.controller

import com.sunfinance.group.verification.system.template.model.dto.TemplateRequestDTO
import com.sunfinance.group.verification.system.template.service.TemplateService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/templates")
class TemplateController(private val templateService: TemplateService) {

    @PostMapping("/render")
    fun getRenderedTemplate(@RequestBody templateRequestDTO: TemplateRequestDTO): ResponseEntity<*> {
        val renderedTemplateDTO = templateService.getRenderedTemplate(templateRequestDTO)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(renderedTemplateDTO.contentType))
            .body(renderedTemplateDTO.content)
    }
}
