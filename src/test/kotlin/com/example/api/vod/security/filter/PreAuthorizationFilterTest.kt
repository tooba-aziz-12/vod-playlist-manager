package com.example.api.vod.security.filter

import com.example.api.vod.constant.RequestHeaders
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
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
        val PERMISSIONS = "permission1, permission2"


        @Nested
        internal inner class FlowTesting {
            @Mock
            private lateinit var mockRequest: HttpServletRequest

            @Mock
            private lateinit var mockResponse: HttpServletResponse

            @Mock
            private lateinit var mockFilterChain: FilterChain

            @Test
            @Throws(Exception::class)
            fun withAllCorrectHeadersPresent() {
                Mockito.`when`(mockRequest.getHeader(ArgumentMatchers.eq(RequestHeaders.USER_ID)))
                    .thenReturn(USER_ID)
                Mockito.`when`(mockRequest.getHeader(ArgumentMatchers.eq(RequestHeaders.PERMISSIONS)))
                    .thenReturn(
                        StringUtils.join(PERMISSIONS)
                    )
                preAuthorizationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain)
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(RequestHeaders.USER_ID))
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(RequestHeaders.PERMISSIONS))
            }
            @Test
            @Throws(Exception::class)
            fun inCaseOfException() {
                Mockito.`when`(mockRequest.getHeader(ArgumentMatchers.eq(RequestHeaders.USER_ID)))
                    .thenThrow(
                        RuntimeException::class.java
                    )
                try{
                    preAuthorizationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain)
                }catch (ex: Exception){
                    Assertions.assertEquals(RuntimeException::class.java, ex::class.java)
                }
                Mockito.verify(mockRequest, Mockito.times(1))
                    .getHeader(ArgumentMatchers.eq(RequestHeaders.USER_ID))
                Mockito.verify(mockRequest, Mockito.times(0))
                    .getHeader(ArgumentMatchers.eq(RequestHeaders.PERMISSIONS))
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
            @Test
            fun allHeadersPresent() {
                httpServletRequest.addHeader(RequestHeaders.USER_ID, USER_ID)
                httpServletRequest.addHeader(RequestHeaders.PERMISSIONS, StringUtils.join(PERMISSIONS))

                preAuthorizationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain)
                Assertions.assertEquals(HttpStatus.OK.value(), httpServletResponse.status)
            }

            @Test
            fun missingHeaders() {
                preAuthorizationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain)
                Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), httpServletResponse.status)
            }

        }
    }
}