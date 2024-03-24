package com.example.api.spring_boot_kotlin_service.controller

import com.example.api.spring_boot_kotlin_service.fixture.UserFixture.Companion.createUserBadDto
import com.example.api.spring_boot_kotlin_service.fixture.UserFixture.Companion.userDto
import com.example.api.spring_boot_kotlin_service.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder


@ExtendWith(
    *arrayOf(MockitoExtension::class,
    RestDocumentationExtension::class
    )
)
class UserControllerTest{

    private val objectMapper  = ObjectMapper()

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var userService: UserService

    companion object {
        const val BASE_URI = "/user"
    }

    @BeforeEach
    fun setUp(
        restDocumentation: RestDocumentationContextProvider
    ) {
        mockMvc = MockMvcBuilders.standaloneSetup(
            UserController(
                userService
            )
        )
            .alwaysDo<StandaloneMockMvcBuilder>(document("{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
            ))
            .apply<StandaloneMockMvcBuilder>(documentationConfiguration(restDocumentation)).build()
    }


    @Nested
    inner class CreateUser {

        @Test
        fun withGoodPayload() {
            val expectedResponse  = userDto.copy()
            expectedResponse.userId = "test-user-id"

            whenever(userService.createUser(userDto)).thenReturn(expectedResponse)

            val andReturn: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("$BASE_URI")
                    .content(objectMapper.writeValueAsString(userDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

            Mockito.verify(userService, times(1)).createUser(userDto)
            Assertions.assertEquals(andReturn.response.status, HttpStatus.OK.value())
        }
        @Test
        fun forBadRequest() {

            val andReturn: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("$BASE_URI")
                    .content(objectMapper.writeValueAsString(createUserBadDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()

            Mockito.verify(userService, times(0)).createUser(createUserBadDto)
            Assertions.assertEquals(andReturn.response.status, HttpStatus.BAD_REQUEST.value())
        }
    }

    @Nested
    inner class GetUser {
        private val requestUserId = "test-user-id"
        private val expectedResponse  = userDto.copy()
        @Test
        fun withGoodPayload() {

            expectedResponse.userId = requestUserId

            whenever(userService.getUserById(requestUserId)).thenReturn(expectedResponse)

            val andReturn: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("$BASE_URI/$requestUserId")
                    .queryParam("id",requestUserId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

            Mockito.verify(userService, times(1)).getUserById(requestUserId)
            Assertions.assertEquals(andReturn.response.status, HttpStatus.OK.value())
        }
        @Test
        fun withWrongURI() {

            val andReturn: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("$BASE_URI")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed)
                .andReturn()

            Mockito.verify(userService, times(0)).getUserById(requestUserId)
            Assertions.assertEquals(andReturn.response.status, HttpStatus.METHOD_NOT_ALLOWED.value())
        }
    }
}