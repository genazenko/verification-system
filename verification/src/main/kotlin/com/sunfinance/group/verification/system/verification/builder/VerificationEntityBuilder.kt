package com.sunfinance.group.verification.system.verification.builder

import com.sunfinance.group.verification.system.verification.configuration.properties.VerificationProperties
import com.sunfinance.group.verification.system.verification.model.constant.VerificationStatus
import com.sunfinance.group.verification.system.verification.model.dto.CreateVerificationDTO
import com.sunfinance.group.verification.system.verification.model.dto.UserInfoDTO
import com.sunfinance.group.verification.system.verification.model.entity.Verification
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random

@Component
class VerificationEntityBuilder(
    private val verificationProperties: VerificationProperties
) {
    fun buildVerificationEntity(
        userInfoDTO: UserInfoDTO,
        createVerificationDTO: CreateVerificationDTO
    ): Verification {
        return Verification(
            generateId(),
            generateCode(verificationProperties.codeLength),
            userInfoDTO.userAgent,
            userInfoDTO.clientIp,
            createVerificationDTO.subject.identity,
            createVerificationDTO.subject.type,
            LocalDateTime.now().plusMinutes(verificationProperties.expirationTimeMinutes),
            VerificationStatus.PENDING,
            0
        )
    }

    private fun generateId(): String {
        return UUID.randomUUID().toString()
    }

    private fun generateCode(length: Int): String {
        return (1..length).map { Random.nextInt(10) }.joinToString("")
    }
}
