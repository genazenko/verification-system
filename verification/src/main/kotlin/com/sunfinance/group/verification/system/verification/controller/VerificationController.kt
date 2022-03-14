package com.sunfinance.group.verification.system.verification.controller

import com.sunfinance.group.verification.system.verification.model.dto.CodeConfirmationDTO
import com.sunfinance.group.verification.system.verification.model.dto.CreateVerificationDTO
import com.sunfinance.group.verification.system.verification.model.dto.UserInfoDTO
import com.sunfinance.group.verification.system.verification.model.dto.VerificationIdDTO
import com.sunfinance.group.verification.system.verification.service.VerificationService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/verifications")
class VerificationController(
    private val verificationService: VerificationService
) {
    @PostMapping
    fun createVerification(
        @RequestHeader(
            HttpHeaders.USER_AGENT,
            required = false
        ) userAgent: String,
        @RequestBody
        createVerificationDTO: CreateVerificationDTO,
        servletRequest: HttpServletRequest
    ): ResponseEntity<VerificationIdDTO> {
        val createdId = verificationService.createVerification(
            UserInfoDTO(
                userAgent,
                servletRequest.remoteAddr
            ), createVerificationDTO
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(VerificationIdDTO(createdId))
    }

    @PutMapping("/{id}")
    fun confirmCode(
        @RequestHeader(
            HttpHeaders.USER_AGENT,
            required = false
        ) userAgent: String,
        @RequestBody
        codeConfirmationDTO: CodeConfirmationDTO,
        @PathVariable("id") id: String,
        servletRequest: HttpServletRequest
    ): ResponseEntity<Unit> {
        verificationService.confirmVerification(
            UserInfoDTO(
                userAgent,
                servletRequest.remoteAddr
            ), id, codeConfirmationDTO.code
        )
        return ResponseEntity.noContent().build()
    }
}
