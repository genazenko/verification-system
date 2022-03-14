package com.sunfinance.group.verification.system.verification.validation.impl

import com.sunfinance.group.verification.system.verification.exception.InvalidIdentityException
import com.sunfinance.group.verification.system.verification.model.constant.IdentityType
import com.sunfinance.group.verification.system.verification.validation.IdentityValidator

class EmailIdentityValidator : IdentityValidator {
    override fun getIdentityType(): IdentityType = IDENTITY

    override fun validate(identity: String) {
        if (!identity.matches(EMAIL_REGEX)) {
            throw InvalidIdentityException(VALIDATION_ERROR_MESSAGE)
        }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^(.+)@(\\\\S+)\$")
        private val IDENTITY = IdentityType.email_confirmation
        private const val VALIDATION_ERROR_MESSAGE = "Invalid email identity passed"
    }
}
