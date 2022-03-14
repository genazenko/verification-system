package com.sunfinance.group.verification.system.verification.model.dto

import com.sunfinance.group.verification.system.verification.model.constant.IdentityType


data class SubjectDTO(
    val identity: String,
    val type: IdentityType
)
