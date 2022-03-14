package com.sunfinance.group.verification.system.verification.service

import com.sunfinance.group.verification.system.verification.model.dto.CreateVerificationDTO
import com.sunfinance.group.verification.system.verification.model.dto.UserInfoDTO

interface VerificationService {
    /**
     * @param userInfoDTO - container for user agent and ip address
     * @param createVerificationDTO - DTO with verification info
     * @return verification id
     */
    fun createVerification(
        userInfoDTO: UserInfoDTO,
        createVerificationDTO: CreateVerificationDTO
    ): String

    /**
     * @param userInfoDTO - container for user agent and ip address
     * @param id - verification id
     * @param code - verification code
     */
    fun confirmVerification(userInfoDTO: UserInfoDTO, id: String, code: String)
}
