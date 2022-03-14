package com.sunfinance.group.verification.system.verification.validation

import com.sunfinance.group.verification.system.verification.model.constant.IdentityType

interface IdentityValidator {
    /**
     * @return - identity type, which can be validated by this service
     */
    fun getIdentityType(): IdentityType

    /**
     * @param identity - identity for validation
     */
    fun validate(identity: String)
}
