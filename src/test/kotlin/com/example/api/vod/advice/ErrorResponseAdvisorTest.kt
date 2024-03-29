package com.example.api.vod.advice

import ch.qos.logback.core.spi.ErrorCodes
import com.example.api.vod.constant.ErrorCode
import com.example.api.vod.dto.ErrorMessageDto
import com.example.api.vod.exception.*
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.platform.commons.util.ReflectionUtils
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException


@ExtendWith(
        *arrayOf(
                MockitoExtension::class
        )
)
class ErrorResponseAdvisorTest{

    private lateinit var  errorResponseAdvisor: ErrorResponseAdvisor

    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        errorResponseAdvisor = ErrorResponseAdvisor()
    }

    @Test
    fun withExceptionHavingBindingResult_returnsResponseEntityWithErrors() {
        val exception = initializeMethodArgumentInvalidException()
        val responseEntity: ResponseEntity<Any> = errorResponseAdvisor.handleMethodArgumentNotValid(exception)
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.statusCode)
        val body: JsonNode = objectMapper.convertValue<JsonNode>(
            responseEntity.body,
            JsonNode::class.java
        )
        Assertions.assertEquals(
            "Argument Invalid",
            body["message"][0].asText()
        )
        Assertions.assertEquals(
            HttpStatus.BAD_REQUEST.name,
            body["httpStatusCode"].asText()
        )
        Assertions.assertEquals(
            ErrorCode.METHOD_ARGUMENT_INVALID.name,
            body["errorCode"].asText()
        )
    }

    private fun initializeMethodArgumentInvalidException(): MethodArgumentNotValidException {
        val method = ReflectionUtils.findMethod(
            javaClass, "testMethodForMethodArgumentNotValidException",
            String::class.java
        ).get()
        val parameter = MethodParameter(method, 0)
        val bindingResult = Mockito.mock(BindingResult::class.java)
        val fieldError = FieldError(
            "objectName",
            "field",
            "Argument Invalid"

        )
        Mockito.`when`(bindingResult.fieldErrors).thenReturn(listOf(fieldError))
        return MethodArgumentNotValidException(parameter, bindingResult)
    }


    private fun testMethodForMethodArgumentNotValidException(methodParam: String): String {
        return methodParam
    }

    @Test
    fun errorResponseFailedToFindPlaylistException() {
        val exception = FailedToFindPlaylistException("play-list-id")
        val responseEntity: ResponseEntity<ErrorMessageDto> =
            errorResponseAdvisor.handleFailedToFindPlaylistException(exception)

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.statusCode)
        Assertions.assertEquals(responseEntity.body!!.errorCode, ErrorCode.PLAYLIST_SEARCH_FAILED.name)

    }

    @Test
    fun errorResponseFailedToSavePlaylistException() {
        val exception = FailedToSavePlaylistException("play-list-name")
        val responseEntity: ResponseEntity<ErrorMessageDto> =
            errorResponseAdvisor.handleFailedToSavePlaylistException(exception)

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.statusCode)
        Assertions.assertEquals(responseEntity.body!!.errorCode, ErrorCode.PLAYLIST_SAVE_FAILED.name)

    }

    @Test
    fun errorResponseFailedToSavePlaylistItemException() {
        val exception = FailedToSavePlaylistItemException("play-list-name")
        val responseEntity: ResponseEntity<ErrorMessageDto> =
            errorResponseAdvisor.handleFailedToSavePlaylistItemException(exception)

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.statusCode)
        Assertions.assertEquals(responseEntity.body!!.errorCode, ErrorCode.PLAYLIST_ITEM_SAVE_FAILED.name)

    }

    @Test
    fun errorResponsePlaylistItemNotFoundException() {
        val exception = PlaylistItemNotFoundException("play-list-id")
        val responseEntity: ResponseEntity<ErrorMessageDto> =
            errorResponseAdvisor.handlePlaylistItemNotFoundException(exception)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.statusCode)
        Assertions.assertEquals(responseEntity.body!!.errorCode, ErrorCode.PLAYLIST_ITEM_NOT_FOUND.name)

    }

    @Test
    fun errorResponsePlaylistNotFoundException() {
        val exception = PlaylistNotFoundException("play-list-id")
        val responseEntity: ResponseEntity<ErrorMessageDto> =
            errorResponseAdvisor.handlePlaylistNotFoundException(exception)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.statusCode)
        Assertions.assertEquals(responseEntity.body!!.errorCode, ErrorCode.PLAYLIST_NOT_FOUND.name)
    }

    @Test
    fun errorResponseInvalidFieldValueException() {
        val errorMessage = "Invalid field value"
        val exception = InvalidFieldValueException(errorMessage = errorMessage)
        val responseEntity: ResponseEntity<ErrorMessageDto> =
            errorResponseAdvisor.handleInvalidFieldValueException(exception)

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.statusCode)
        Assertions.assertEquals(responseEntity.body!!.errorCode, ErrorCode.INVALID_FIELD_VALUE.name)
        Assertions.assertEquals(responseEntity.body!!.message[0], errorMessage)
    }

}