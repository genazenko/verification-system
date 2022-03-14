package com.sunfinance.group.verification.system.verification.service.impl

import com.sunfinance.group.verification.system.verification.exception.InvalidCodeException
import com.sunfinance.group.verification.system.verification.exception.NoPermissionToConfirmException
import com.sunfinance.group.verification.system.verification.exception.VerificationExpiredException
import com.sunfinance.group.verification.system.verification.model.constant.VerificationStatus
import com.sunfinance.group.verification.system.verification.model.dto.UserInfoDTO
import com.sunfinance.group.verification.system.verification.model.entity.Verification
import com.sunfinance.group.verification.system.verification.service.CodeVerificationService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CodeVerificationServiceImpl : CodeVerificationService {
    override fun verifyCode(verification: Verification, code: String, userInfoDTO: UserInfoDTO) {
        checkPermission(verification, userInfoDTO)
        checkExpiration(verification)
        checkConfirmation(verification, code)
    }

    private fun checkPermission(verification: Verification, userInfoDTO: UserInfoDTO) {
        if (verification.userAgent != userInfoDTO.userAgent || verification.clientIp != userInfoDTO.clientIp) {
            throw NoPermissionToConfirmException(PERMISSION_DENIED_MESSAGE)
        }
    }

    private fun checkExpiration(verification: Verification) {
        if (verification.status == VerificationStatus.EXPIRED
            || (verification.status == VerificationStatus.PENDING
                && verification.expirationTime.isBefore(LocalDateTime.now()))
        ) {
            throw VerificationExpiredException(CODE_EXPIRED_MESSAGE)
        }
    }

    private fun checkConfirmation(verification: Verification, code: String) {
        if (verification.code != code) {
            throw InvalidCodeException(INVALID_CODE_MESSAGE)
        }
    }

    companion object {
        private const val PERMISSION_DENIED_MESSAGE = "Different user agent or IP"
        private const val CODE_EXPIRED_MESSAGE = "Code is expired"
        private const val INVALID_CODE_MESSAGE = "Confirmation code is invalid"
    }
}
