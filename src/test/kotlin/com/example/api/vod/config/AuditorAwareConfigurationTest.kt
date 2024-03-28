package com.example.api.vod.config

import com.example.api.vod.constant.RequestHeaders
import com.example.api.vod.security.filter.PreAuthorizationFilter
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
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

    private val objectMapper = ObjectMapper()

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
        val userIdHeader = "test-user-id"
        val permissionsHeader = "permission1,permission2"

        `when`(mockHttpRequest.getHeader(RequestHeaders.USER_ID)).thenReturn(userIdHeader)
        `when`(mockHttpRequest.getHeader(RequestHeaders.PERMISSIONS)).thenReturn(permissionsHeader)

        preAuthorizationFilter.doFilterInternal(mockHttpRequest, mockHttpResponse, mockFilterChain)
        Mockito.verify(mockFilterChain, times(1)).doFilter(mockHttpRequest,mockHttpResponse)
        Assertions.assertEquals(Optional.of(userIdHeader), auditorAwareImpl.currentAuditor)
    }

    @AfterEach
    fun clearContextAfter() {
        SecurityContextHolder.clearContext()
    }

}