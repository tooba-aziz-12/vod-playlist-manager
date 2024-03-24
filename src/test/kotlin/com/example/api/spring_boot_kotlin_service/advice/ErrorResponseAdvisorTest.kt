package com.example.api.spring_boot_kotlin_service.advice

import com.example.api.spring_boot_kotlin_service.constant.ErrorCode
import com.example.api.spring_boot_kotlin_service.dto.ErrorMessageDto
import com.example.api.spring_boot_kotlin_service.exception.UserCreationFailedException
import com.example.api.spring_boot_kotlin_service.exception.UserNotFoundException
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
    fun handleUserNotFoundException(){

        val responseMessage = "User not Found"

        val exception = UserNotFoundException(errorMessage = responseMessage)
        val responseEntity: ResponseEntity<ErrorMessageDto> =
                errorResponseAdvisor.handleUserNotFoundException(exception)

        equateErrorResponse(responseEntity, responseMessage,HttpStatus.BAD_REQUEST, ErrorCode.USER_NOT_FOUND)

    }

    @Test
    fun handleUserCreationFailedException(){

        val responseMessage = "User creation failed"

        val exception = UserCreationFailedException(errorMessage = responseMessage)
        val responseEntity: ResponseEntity<ErrorMessageDto> =
            errorResponseAdvisor.handleUserCreationFailedException(exception)

        equateErrorResponse(responseEntity, responseMessage,HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.USER_CREATION_FAILED)

    }

    private fun equateErrorResponse(
        responseEntity: ResponseEntity<ErrorMessageDto>,
        responseBody: String,
        httpStatus: HttpStatus,
        errorCodes: ErrorCode
    ) {
        Assertions.assertEquals(errorCodes.name, responseEntity.body!!.errorCode)
        Assertions.assertEquals(responseBody, responseEntity.body!!.message[0])
        Assertions.assertEquals(httpStatus.name, responseEntity.body!!.httpStatusCode)
        Assertions.assertEquals(httpStatus, responseEntity.statusCode)
    }

}