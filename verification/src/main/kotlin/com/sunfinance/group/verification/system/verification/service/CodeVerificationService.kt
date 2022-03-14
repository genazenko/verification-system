package com.sunfinance.group.verification.system.verification.service

import com.sunfinance.group.verification.system.verification.model.dto.UserInfoDTO
import com.sunfinance.group.verification.system.verification.model.entity.Verification

/**
 * Service for code verification. Checks if code is correct and user info is matched with original
 */
interface CodeVerificationService {
    /**
     * @param verification - verification entity
     * @param code - verification code from user
     * @param userInfoDTO - container for user agent and ip address
     */
    fun verifyCode(verification: Verification, code: String, userInfoDTO: UserInfoDTO)
}
