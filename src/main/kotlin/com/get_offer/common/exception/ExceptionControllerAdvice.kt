package com.get_offer.common.exception

import ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.message ?: "DEFAULT ERROR"))
    }

    @ExceptionHandler
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.message))
    }

    @ExceptionHandler
    fun handleUnAuthorizationException(ex: UnAuthorizationException): ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("인가되지 않은 사용자입니다"))
    }
}