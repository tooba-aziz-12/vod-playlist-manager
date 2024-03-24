package com.example.api.spring_boot_kotlin_service.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.AdditionalMatchers
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class PreAuthorizationFilterTest {
    private lateinit var preAuthorizationFilter: PreAuthorizationFilter
    @BeforeEach
    fun beforeEach() {
        preAuthorizationFilter = PreAuthorizationFilter()
    }

    @Nested
    internal inner class DoFilterInternal {
        val USER_ID = "b42a435465"
        val PERMISSIONS = Arrays.asList("permission-1", "permission-2")

        @Nested
        internal inner class FlowTesting {
            @Mock
            private lateinit var mockRequest: HttpServletRequest

            @Mock
            private lateinit var mockResponse: HttpServletResponse

            @Mock
            private lateinit var mockFilterChain: FilterChain

            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = ["  ", "false", "FALSE", "some-invalid-value"])
            @Throws(
                Exception::class
            )
            fun isValidatedWithInvalidValueOrFalse(isValidated: String?) {
                Mockito.`when`(mockRequest.getHeader(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.IS_VALIDATED)))
                    .thenReturn(isValidated)
                preAuthorizationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain)
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.IS_VALIDATED))
                Mockito.verify(mockRequest, Mockito.never())
                    .getHeader(AdditionalMatchers.not(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.IS_VALIDATED)))
                Mockito.verify(mockResponse, Mockito.never())
                    .sendError(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())
                Mockito.verify(mockFilterChain, Mockito.times(1))
                    .doFilter(ArgumentMatchers.eq(mockRequest), ArgumentMatchers.eq(mockResponse))
            }

            @ParameterizedTest
            @ValueSource(strings = ["true", "TRUE", "tRuE"])
            @Throws(
                Exception::class
            )
            fun isValidatedWithTrueValue(isValidated: String) {
                Mockito.`when`(mockRequest.getHeader(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.IS_VALIDATED)))
                    .thenReturn(isValidated)
                Mockito.`when`(mockRequest.getHeader(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_ID)))
                    .thenReturn(USER_ID)
                Mockito.`when`(mockRequest.getHeader(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_PERMISSIONS)))
                    .thenReturn(
                        StringUtils.join(PERMISSIONS, ",")
                    )
                preAuthorizationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain)
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.IS_VALIDATED))
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_ID))
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_PERMISSIONS))
                Mockito.verify(mockResponse, Mockito.never())
                    .sendError(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())
                Mockito.verify(mockFilterChain, Mockito.times(1))
                    .doFilter(ArgumentMatchers.eq(mockRequest), ArgumentMatchers.eq(mockResponse))
            }

            @Test
            @Throws(Exception::class)
            fun inCaseOfException() {
                Mockito.`when`(mockRequest.getHeader(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.IS_VALIDATED)))
                    .thenThrow(
                        RuntimeException::class.java
                    )
                try{
                    preAuthorizationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain)
                }catch (ex: Exception){
                    Assertions.assertEquals(RuntimeException::class.java, ex::class.java)
                }
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.IS_VALIDATED))
                Mockito.verify(mockRequest, Mockito.times(0))
                    .getHeader(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_ID))
                Mockito.verify(mockRequest, Mockito.times(0))
                    .getHeader(ArgumentMatchers.eq(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_PERMISSIONS))
                Mockito.verify(mockFilterChain, Mockito.times(0))
                    .doFilter(ArgumentMatchers.eq(mockRequest), ArgumentMatchers.eq(mockResponse))

            }
        }

        @Nested
        internal inner class ResultTesting {
            private lateinit var httpServletRequest: MockHttpServletRequest
            private lateinit var httpServletResponse: MockHttpServletResponse
            private lateinit var filterChain: MockFilterChain
            @BeforeEach
            fun beforeEach() {
                httpServletRequest = MockHttpServletRequest()
                httpServletResponse = MockHttpServletResponse()
                filterChain = MockFilterChain()
            }

            @ParameterizedTest
            @ValueSource(strings = ["true", "TRUE", "tRuE"])
            fun isValidatedWithAllHeadersPresent(isValidated: String) {
                httpServletRequest.addHeader(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.IS_VALIDATED, isValidated)
                httpServletRequest.addHeader(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_ID, USER_ID)
                httpServletRequest.addHeader(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_PERMISSIONS, StringUtils.join(PERMISSIONS, ","))
                preAuthorizationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain)
                Assertions.assertEquals(HttpStatus.OK.value(), httpServletResponse.status)
            }

            @ParameterizedTest
            @ValueSource(strings = ["", " ", "false", "some-invalid-value"])
            @Throws(
                Exception::class
            )
            fun isNotValidated(isValidated: String?) {
                httpServletRequest.addHeader(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.IS_VALIDATED, isValidated!!)
                httpServletRequest.addHeader(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_ID, USER_ID)
                httpServletRequest.addHeader(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_PERMISSIONS, StringUtils.join(PERMISSIONS, ","))
                preAuthorizationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain)
                Assertions.assertEquals(HttpStatus.OK.value(), httpServletResponse.status)
            }

        }
    }
}