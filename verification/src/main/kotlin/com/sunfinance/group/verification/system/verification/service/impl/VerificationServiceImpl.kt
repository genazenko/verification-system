package com.sunfinance.group.verification.system.verification.service.impl

import com.sunfinance.group.verification.system.verification.builder.VerificationEntityBuilder
import com.sunfinance.group.verification.system.verification.configuration.properties.VerificationProperties
import com.sunfinance.group.verification.system.verification.exception.InvalidCodeException
import com.sunfinance.group.verification.system.verification.exception.NoPermissionToConfirmException
import com.sunfinance.group.verification.system.verification.exception.VerificationExpiredException
import com.sunfinance.group.verification.system.verification.exception.VerificationNotFoundException
import com.sunfinance.group.verification.system.verification.model.constant.IdentityType
import com.sunfinance.group.verification.system.verification.model.constant.VerificationStatus
import com.sunfinance.group.verification.system.verification.model.dto.CreateVerificationDTO
import com.sunfinance.group.verification.system.verification.model.dto.UserInfoDTO
import com.sunfinance.group.verification.system.verification.repository.VerificationRepository
import com.sunfinance.group.verification.system.verification.service.CodeVerificationService
import com.sunfinance.group.verification.system.verification.service.EventSenderService
import com.sunfinance.group.verification.system.verification.service.VerificationService
import com.sunfinance.group.verification.system.verification.validation.IdentityValidator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VerificationServiceImpl(
    private val verificationRepository: VerificationRepository,
    private val identityValidators: Map<IdentityType, IdentityValidator>,
    private val verificationEntityBuilder: VerificationEntityBuilder,
    private val codeVerificationService: CodeVerificationService,
    private val eventSenderService: EventSenderService,
    private val verificationProperties: VerificationProperties
) : VerificationService {

    @Transactional
    override fun createVerification(
        userInfoDTO: UserInfoDTO,
        createVerificationDTO: CreateVerificationDTO
    ): String {
        identityValidators[createVerificationDTO.subject.type]?.validate(createVerificationDTO.subject.identity)
        val entity =
            verificationEntityBuilder.buildVerificationEntity(userInfoDTO, createVerificationDTO)
        verificationRepository.createVerification(entity)
        eventSenderService.sendVerificationCreatedEvent(entity)
        return entity.id
    }

    @Transactional(
        noRollbackFor = [InvalidCodeException::class,
            NoPermissionToConfirmException::class,
            VerificationExpiredException::class]
    )
    override fun confirmVerification(userInfoDTO: UserInfoDTO, id: String, code: String) {
        verificationRepository
            .findById(id)
            .ifPresentOrElse({
                try {
                    codeVerificationService.verifyCode(it, code, userInfoDTO)
                    verificationRepository.updateVerificationStatus(
                        id,
                        VerificationStatus.CONFIRMED
                    )
                    eventSenderService.sendVerificationConfirmedEvent(it)
                } catch (ex: Exception) {
                    when (ex) {
                        is InvalidCodeException -> {
                            verificationRepository.increaseAttempt(id)
                            if (it.confirmationAttempt >= verificationProperties.attemptCount - 1) {
                                verificationRepository.updateVerificationStatus(
                                    id,
                                    VerificationStatus.EXPIRED
                                )
                            }
                        }
                        is VerificationExpiredException -> {
                            verificationRepository.updateVerificationStatus(
                                id,
                                VerificationStatus.EXPIRED
                            )
                        }
                    }
                    eventSenderService.sendVerificationConfirmationFailedEvent(it)
                    throw ex
                }
            }, {
                throw VerificationNotFoundException(VERIFICATION_NOT_FOUND_MESSAGE)
            })
    }

    companion object {
        private const val VERIFICATION_NOT_FOUND_MESSAGE = "Verification is not found"
    }
}
