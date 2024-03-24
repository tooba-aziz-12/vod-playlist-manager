package com.example.api.vod.config

import com.example.api.vod.security.filter.PreAuthorizationFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*


internal class AuditorAwareConfigurationTest {

    private val auditorAwareImpl = AuditorAwareConfiguration()

    private lateinit var preAuthorizationFilter : PreAuthorizationFilter
    private lateinit var applicationSecurityConfiguration: com.example.api.vod.security.config.ApplicationSecurityConfiguration

    private val mockHttpRequest: HttpServletRequest = mock(HttpServletRequest::class.java)
    private val mockHttpResponse: HttpServletResponse = mock(HttpServletResponse::class.java)
    private val mockFilterChain: FilterChain = mock(FilterChain::class.java)


    @BeforeEach
    fun beforeEach() {
        SecurityContextHolder.clearContext()
        preAuthorizationFilter = PreAuthorizationFilter()
        applicationSecurityConfiguration =
            com.example.api.vod.security.config.ApplicationSecurityConfiguration(
                preAuthorizationFilter
            )

    }

    @Test
    fun currentAuditorShouldExist() {
        val userIdHeader = "Tooba"

        `when`(mockHttpRequest.getHeader(com.example.api.vod.constant.RequestHeaders.USER_ID)).thenReturn(userIdHeader)
        preAuthorizationFilter.doFilterInternal(mockHttpRequest, mockHttpResponse, mockFilterChain)
        Mockito.verify(mockFilterChain, times(1)).doFilter(mockHttpRequest,mockHttpResponse)
        Assertions.assertEquals(Optional.of(userIdHeader), auditorAwareImpl.currentAuditor)
    }

    @Test
    fun currentAuditorShouldNotExist() {
        val userIdHeader = ""

        `when`(mockHttpRequest.getHeader(com.example.api.vod.constant.RequestHeaders.USER_ID)).thenReturn(userIdHeader)
        preAuthorizationFilter.doFilterInternal(mockHttpRequest, mockHttpResponse, mockFilterChain)
        Mockito.verify(mockFilterChain, times(1)).doFilter(mockHttpRequest,mockHttpResponse)
        Assertions.assertTrue(auditorAwareImpl.currentAuditor.isEmpty)

    }

    @AfterEach
    fun clearContextAfter() {
        SecurityContextHolder.clearContext()
    }

}