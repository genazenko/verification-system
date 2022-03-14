package com.sunfinance.group.verification.system.template.repository

import com.sunfinance.group.verification.system.template.model.entity.Template
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TemplateRepository : CrudRepository<Template, Long> {
    fun findOneBySlug(slug: String): Optional<Template>
}
