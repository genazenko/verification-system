package com.sunfinance.group.verification.system.verification.repository

import com.sunfinance.group.verification.system.verification.model.constant.VerificationStatus
import com.sunfinance.group.verification.system.verification.model.entity.Verification
import java.util.Optional


interface VerificationRepository {
    fun findById(id: String): Optional<Verification>

    /**
     * Creates new verification entity if PENDING, not EXPIRED verification doesn't exist.
     * If verification with same subject has at least one successful confirmation, creates a new one.
     */
    fun createVerification(verification: Verification): String?
    fun updateVerificationStatus(id: String, status: VerificationStatus): Boolean
    fun increaseAttempt(id: String): Boolean
}
