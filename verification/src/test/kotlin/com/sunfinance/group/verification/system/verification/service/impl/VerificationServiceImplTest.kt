package com.sunfinance.group.verification.system.verification.service.impl

import com.sunfinance.group.verification.system.verification.builder.VerificationEntityBuilder
import com.sunfinance.group.verification.system.verification.configuration.properties.VerificationProperties
import com.sunfinance.group.verification.system.verification.exception.InvalidCodeException
import com.sunfinance.group.verification.system.verification.exception.InvalidIdentityException
import com.sunfinance.group.verification.system.verification.exception.NoPermissionToConfirmException
import com.sunfinance.group.verification.system.verification.exception.VerificationExpiredException
import com.sunfinance.group.verification.system.verification.exception.VerificationNotFoundException
import com.sunfinance.group.verification.system.verification.model.constant.IdentityType
import com.sunfinance.group.verification.system.verification.model.constant.VerificationStatus
import com.sunfinance.group.verification.system.verification.model.dto.CreateVerificationDTO
import com.sunfinance.group.verification.system.verification.model.dto.SubjectDTO
import com.sunfinance.group.verification.system.verification.model.dto.UserInfoDTO
import com.sunfinance.group.verification.system.verification.model.entity.Verification
import com.sunfinance.group.verification.system.verification.repository.VerificationRepository
import com.sunfinance.group.verification.system.verification.service.EventSenderService
import com.sunfinance.group.verification.system.verification.service.VerificationService
import com.sunfinance.group.verification.system.verification.validation.impl.EmailIdentityValidator
import com.sunfinance.group.verification.system.verification.validation.impl.MobileIdentityValidator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import java.time.LocalDateTime
import java.util.Optional

internal class VerificationServiceImplTest {
    private val verificationRepositoryMock = mock(VerificationRepository::class.java)
    private val identityValidators = mapOf(
        IdentityType.email_confirmation to EmailIdentityValidator(),
        IdentityType.mobile_confirmation to MobileIdentityValidator()
    )
    private val verificationProperties = VerificationProperties()
    private val verificationEntityBuilder = VerificationEntityBuilder(verificationProperties)
    private val codeVerificationService = CodeVerificationServiceImpl()
    private val eventSenderService = mock(EventSenderService::class.java)

    private val verificationService: VerificationService = VerificationServiceImpl(
        verificationRepositoryMock,
        identityValidators,
        verificationEntityBuilder,
        codeVerificationService,
        eventSenderService,
        verificationProperties
    )

    @Test
    fun `create new verification`() {
        `when`(verificationRepositoryMock.createVerification(any())).thenAnswer {
            it.getArgument(
                0,
                Verification::class.java
            ).id
        }
        val userInfoDTO = UserInfoDTO("agent", "ip")
        val createVerificationDTO =
            CreateVerificationDTO(SubjectDTO("+78912", IdentityType.mobile_confirmation))
        val id = verificationService.createVerification(userInfoDTO, createVerificationDTO)
        assert(id.isNotEmpty())
    }

    @Test
    fun `invalid mobile identity`() {
        `when`(verificationRepositoryMock.createVerification(any())).thenAnswer {
            it.getArgument(
                0,
                Verification::class.java
            ).id
        }
        val userInfoDTO = UserInfoDTO("agent", "ip")
        val createVerificationDTO =
            CreateVerificationDTO(SubjectDTO("akjshd", IdentityType.mobile_confirmation))
        Assertions.assertThrows(InvalidIdentityException::class.java) {
            verificationService.createVerification(
                userInfoDTO,
                createVerificationDTO
            )
        }
    }

    @Test
    fun `invalid email identity`() {
        `when`(verificationRepositoryMock.createVerification(any())).thenAnswer {
            it.getArgument(
                0,
                Verification::class.java
            ).id
        }
        val userInfoDTO = UserInfoDTO("agent", "ip")
        val createVerificationDTO =
            CreateVerificationDTO(SubjectDTO("akjshd", IdentityType.email_confirmation))
        Assertions.assertThrows(InvalidIdentityException::class.java) {
            verificationService.createVerification(
                userInfoDTO,
                createVerificationDTO
            )
        }
    }

    @Test
    fun `success confirmation`() {
        val userInfoDTO = UserInfoDTO("agent", "ip")
        val verification = Verification(
            "id1",
            "code1",
            userInfoDTO.userAgent,
            userInfoDTO.clientIp,
            "identity",
            IdentityType.mobile_confirmation,
            LocalDateTime.now().plusMinutes(5),
            VerificationStatus.PENDING,
            0
        )

        `when`(verificationRepositoryMock.findById(verification.id)).thenReturn(
            Optional.of(
                verification
            )
        )

        verificationService.confirmVerification(userInfoDTO, verification.id, verification.code)
        Mockito.verify(verificationRepositoryMock, times(1))
            .updateVerificationStatus(
                verification.id,
                VerificationStatus.CONFIRMED
            )
        Mockito.verify(eventSenderService, times(1))
            .sendVerificationConfirmedEvent(verification)
    }

    @Test
    fun `not found exception`() {
        val userInfoDTO = UserInfoDTO("agent", "ip")

        `when`(verificationRepositoryMock.findById(any())).thenReturn(
            Optional.empty()
        )

        val thrown = Assertions.assertThrows(VerificationNotFoundException::class.java) {
            verificationService.confirmVerification(userInfoDTO, "some_id", "some_code")
        }

        assert(thrown.message == "Verification is not found")
    }

    @Test
    fun `invalid code exception`() {
        val userInfoDTO = UserInfoDTO("agent", "ip")
        val verification = Verification(
            "id1",
            "code1",
            userInfoDTO.userAgent,
            userInfoDTO.clientIp,
            "identity",
            IdentityType.mobile_confirmation,
            LocalDateTime.now().plusMinutes(5),
            VerificationStatus.PENDING,
            0
        )

        `when`(verificationRepositoryMock.findById(verification.id)).thenReturn(
            Optional.of(verification)
        )

        val thrown = Assertions.assertThrows(InvalidCodeException::class.java) {
            verificationService.confirmVerification(userInfoDTO, verification.id, "some_code")
        }

        assert(thrown.message == "Confirmation code is invalid")
    }

    @Test
    fun `verification expired exception`() {
        val userInfoDTO = UserInfoDTO("agent", "ip")
        val verification = Verification(
            "id1",
            "code1",
            userInfoDTO.userAgent,
            userInfoDTO.clientIp,
            "identity",
            IdentityType.mobile_confirmation,
            LocalDateTime.now().minusMinutes(5),
            VerificationStatus.PENDING,
            0
        )

        `when`(verificationRepositoryMock.findById(verification.id)).thenReturn(
            Optional.of(verification)
        )

        val thrown = Assertions.assertThrows(VerificationExpiredException::class.java) {
            verificationService.confirmVerification(userInfoDTO, verification.id, verification.code)
        }

        assert(thrown.message == "Code is expired")
    }

    @Test
    fun `permission denied exception`() {
        val userInfoDTO = UserInfoDTO("agent", "ip")
        val verification = Verification(
            "id1",
            "code1",
            "diff-agent",
            userInfoDTO.clientIp,
            "identity",
            IdentityType.mobile_confirmation,
            LocalDateTime.now().minusMinutes(5),
            VerificationStatus.PENDING,
            0
        )

        `when`(verificationRepositoryMock.findById(verification.id)).thenReturn(
            Optional.of(verification)
        )

        val thrown = Assertions.assertThrows(NoPermissionToConfirmException::class.java) {
            verificationService.confirmVerification(userInfoDTO, verification.id, verification.code)
        }

        assert(thrown.message == "Different user agent or IP")
    }
}
