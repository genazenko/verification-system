package com.sunfinance.group.verification.system.template.model.dto

data class TemplateRequestDTO(
    val slug: String?,
    val variables: Map<String, String>?
)
