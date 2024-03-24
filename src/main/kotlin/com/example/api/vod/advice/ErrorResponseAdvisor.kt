package com.example.api.vod.advice

import com.example.api.vod.constant.ErrorCode
import com.example.api.vod.dto.ErrorMessageDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.Collectors

@RestControllerAdvice(basePackages = ["com.example.api.vod"])

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
}