package com.sunfinance.group.verification.system.verification.model.entity

import com.sunfinance.group.verification.system.verification.model.constant.IdentityType
import com.sunfinance.group.verification.system.verification.model.constant.VerificationStatus
import java.time.LocalDateTime

data class Verification(
    val id: String,
    val code: String,
    val userAgent: String,
    val clientIp: String,
    val identity: String,
    val type: IdentityType,
    val expirationTime: LocalDateTime,
    val status: VerificationStatus,
    val confirmationAttempt: Int
)
