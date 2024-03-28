package com.example.api.vod.advice

import com.example.api.vod.constant.ErrorCode
import com.example.api.vod.dto.ErrorMessageDto
import com.example.api.vod.exception.*
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

    @ExceptionHandler(
        FailedToSavePlaylistException::class
    )
    fun handleFailedToSavePlaylistException(ex: FailedToSavePlaylistException): ResponseEntity<ErrorMessageDto> {
        val errorMessage = ErrorMessageDto(
            HttpStatus.INTERNAL_SERVER_ERROR.name,
            mutableListOf(ex.message),
            ex.errorCode,
        )
        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(
        FailedToSavePlaylistItemException::class
    )
    fun handleFailedToSavePlaylistItemException(ex: FailedToSavePlaylistItemException): ResponseEntity<ErrorMessageDto> {
        val errorMessage = ErrorMessageDto(
            HttpStatus.INTERNAL_SERVER_ERROR.name,
            mutableListOf(ex.message),
            ex.errorCode,
        )
        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(
        FailedToFindPlaylistException::class
    )
    fun handleFailedToFindPlaylistException(ex: FailedToFindPlaylistException): ResponseEntity<ErrorMessageDto> {

        val errorMessage = ErrorMessageDto(
            HttpStatus.INTERNAL_SERVER_ERROR.name,
            mutableListOf(ex.message),
            ex.errorCode,
        )
        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }
    @ExceptionHandler(
        PlaylistNotFoundException::class
    )
    fun handlePlaylistNotFoundException(ex: PlaylistNotFoundException): ResponseEntity<ErrorMessageDto> {
        val errorMessage = ErrorMessageDto(
            HttpStatus.NOT_FOUND.name,
            mutableListOf(ex.message),
            ex.errorCode,
        )
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(
        PlaylistItemNotFoundException::class
    )
    fun handlePlaylistItemNotFoundException(ex: PlaylistItemNotFoundException): ResponseEntity<ErrorMessageDto> {
        val errorMessage = ErrorMessageDto(
            HttpStatus.NOT_FOUND.name,
            mutableListOf(ex.message),
            ex.errorCode,
        )
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }
}