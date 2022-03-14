package com.sunfinance.group.verification.system.verification.configuration

import com.sunfinance.group.verification.system.verification.model.constant.IdentityType
import com.sunfinance.group.verification.system.verification.validation.IdentityValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class IdentityValidatorBeanConfiguration {

    @Bean
    fun identityValidatorMap(identityValidators: List<IdentityValidator>): Map<IdentityType, IdentityValidator> =
        identityValidators.associateBy { it.getIdentityType() }
}
