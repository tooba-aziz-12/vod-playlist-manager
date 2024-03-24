package com.example.api.spring_boot_kotlin_service.advice

import com.example.api.spring_boot_kotlin_service.constant.ErrorCode
import com.example.api.spring_boot_kotlin_service.dto.ErrorMessageDto
import com.example.api.spring_boot_kotlin_service.exception.UserCreationFailedException
import com.example.api.spring_boot_kotlin_service.exception.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.Collectors

@RestControllerAdvice(basePackages = ["com.example.api.spring_boot_kotlin_service"])

class ErrorResponseAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<Any> {

        val body: MutableMap<String, MutableList<String>> = HashMap()
        val errors = ex.bindingResult
            .fieldErrors
            .stream()
            .map { obj: FieldError -> obj.defaultMessage }
            .collect(Collectors.toList())
        body["errors"] = errors as MutableList<String>

        val errorMessage = ErrorMessageDto(
            HttpStatus.BAD_REQUEST.name,
            errors,
            ErrorCode.METHOD_ARGUMENT_INVALID.name
        )

        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(
        UserNotFoundException::class
    )
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<ErrorMessageDto> {
        val errorMessage = ErrorMessageDto(
            HttpStatus.BAD_REQUEST.name,
            mutableListOf(ex.message),
            ex.errorCode
        )
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(
        UserCreationFailedException::class
    )
    fun handleUserCreationFailedException(ex: UserCreationFailedException): ResponseEntity<ErrorMessageDto> {
        val errorMessage = ErrorMessageDto(
            HttpStatus.INTERNAL_SERVER_ERROR.name,
            mutableListOf(ex.message),
            ex.errorCode
        )
        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}