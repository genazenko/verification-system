package com.sunfinance.group.verification.system.template.controller

import com.sunfinance.group.verification.system.template.exception.TemplateNotFoundException
import com.sunfinance.group.verification.system.template.exception.TemplateValidationException
import com.sunfinance.group.verification.system.template.model.dto.ErrorResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest


@RestControllerAdvice
class TemplateErrorControllerAdvice {
    @ExceptionHandler(value = [TemplateNotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected fun handleNotFoundError(
        ex: RuntimeException, request: WebRequest
    ): ResponseEntity<ErrorResponseDTO> {
        val responseMessage = ex.message
        return buildResponseEntity(responseMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun malformedJsonException(
        ex: RuntimeException,
        request: WebRequest
    ): ResponseEntity<ErrorResponseDTO> {
        val message = "Malformed JSON request"
        return buildResponseEntity(message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [TemplateValidationException::class])
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected fun validationError(
        ex: RuntimeException,
        request: WebRequest
    ): ResponseEntity<ErrorResponseDTO> {
        val message = ex.message
        return buildResponseEntity(message, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    private fun buildResponseEntity(
        message: String?,
        status: HttpStatus
    ): ResponseEntity<ErrorResponseDTO> =
        ResponseEntity<ErrorResponseDTO>(ErrorResponseDTO(message), status)
}
