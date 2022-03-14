package com.sunfinance.group.verification.system.verification.validation.impl

import com.sunfinance.group.verification.system.verification.exception.InvalidIdentityException
import com.sunfinance.group.verification.system.verification.model.constant.IdentityType
import com.sunfinance.group.verification.system.verification.validation.IdentityValidator
import org.springframework.stereotype.Component

@Component
class MobileIdentityValidator : IdentityValidator {
    override fun getIdentityType(): IdentityType = IDENTITY

    override fun validate(identity: String) {
        if (!identity.matches(MOBILE_PHONE_REGEX)) {
            throw InvalidIdentityException(VALIDATION_ERROR_MESSAGE)
        }
    }

    companion object {
        private val MOBILE_PHONE_REGEX = Regex("[+]\\d+")
        private val IDENTITY = IdentityType.mobile_confirmation
        private const val VALIDATION_ERROR_MESSAGE =
            "Invalid mobile identity passed. Must starts with + and contains only digits characters"
    }
}
