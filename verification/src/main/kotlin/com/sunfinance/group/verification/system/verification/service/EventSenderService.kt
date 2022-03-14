package com.sunfinance.group.verification.system.verification.service

import com.sunfinance.group.verification.system.verification.model.entity.Verification

/**
 * Interface for event sender service.
 */
interface EventSenderService {
    /**
     * Send verificationCreated event
     * @param verification - verification entity
     */
    fun sendVerificationCreatedEvent(verification: Verification)

    /**
     * Send verificationConfirmed event
     * @param verification - verification entity
     */
    fun sendVerificationConfirmedEvent(verification: Verification)

    /**
     * Send verificationFailed event
     * @param verification - verification entity
     */
    fun sendVerificationConfirmationFailedEvent(verification: Verification)
}
