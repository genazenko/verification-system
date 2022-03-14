package com.sunfinance.group.verification.system.verification.controller

import com.sunfinance.group.verification.system.verification.exception.DuplicateVerificationException
import com.sunfinance.group.verification.system.verification.exception.InvalidCodeException
import com.sunfinance.group.verification.system.verification.exception.InvalidIdentityException
import com.sunfinance.group.verification.system.verification.exception.NoPermissionToConfirmException
import com.sunfinance.group.verification.system.verification.exception.VerificationExpiredException
import com.sunfinance.group.verification.system.verification.exception.VerificationNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class VerificationControllerAdvice {
    @ExceptionHandler(value = [DuplicateVerificationException::class])
    @ResponseStatus(HttpStatus.CONFLICT)
    protected fun handleDuplicateVerificationException(
        ex: RuntimeException, request: WebRequest
    ): ResponseEntity<String?> {
        val responseMessage = ex.message
        return buildResponseEntity(responseMessage, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(value = [InvalidIdentityException::class, InvalidCodeException::class])
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected fun handleValidationException(
        ex: RuntimeException,
        request: WebRequest
    ): ResponseEntity<String?> {
        val message = ex.message
        return buildResponseEntity(message, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(value = [NoPermissionToConfirmException::class])
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected fun handlePermissionDenied(
        ex: RuntimeException,
        request: WebRequest
    ): ResponseEntity<String?> {
        val message = ex.message
        return buildResponseEntity(message, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(value = [VerificationNotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected fun handleNotFoundException(
        ex: RuntimeException,
        request: WebRequest
    ): ResponseEntity<String?> {
        val message = ex.message
        return buildResponseEntity(message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [VerificationExpiredException::class])
    @ResponseStatus(HttpStatus.GONE)
    protected fun handleExpiredException(
        ex: RuntimeException,
        request: WebRequest
    ): ResponseEntity<String?> {
        val message = ex.message
        return buildResponseEntity(message, HttpStatus.GONE)
    }

    private fun buildResponseEntity(message: String?, status: HttpStatus): ResponseEntity<String?> =
        ResponseEntity<String?>(message, status)
}
