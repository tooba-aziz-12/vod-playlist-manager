package com.example.api.spring_boot_kotlin_service.config

import com.example.api.spring_boot_kotlin_service.security.filter.PreAuthorizationFilter
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
    private lateinit var applicationSecurityConfiguration: com.example.api.spring_boot_kotlin_service.security.config.ApplicationSecurityConfiguration

    private val mockHttpRequest: HttpServletRequest = mock(HttpServletRequest::class.java)
    private val mockHttpResponse: HttpServletResponse = mock(HttpServletResponse::class.java)
    private val mockFilterChain: FilterChain = mock(FilterChain::class.java)
    private val httpSecurity: HttpSecurity = mock(HttpSecurity::class.java)


    @BeforeEach
    fun beforeEach() {
        SecurityContextHolder.clearContext()
        preAuthorizationFilter = PreAuthorizationFilter()
        applicationSecurityConfiguration =
            com.example.api.spring_boot_kotlin_service.security.config.ApplicationSecurityConfiguration(
                preAuthorizationFilter
            )

    }

    @Test
    fun currentAuditorShouldExist() {
        val userIdHeader = "Tooba"
        val isValidatedHeader = "true"
        val userPermissionsHeader = "permission1,permission2"

        `when`(mockHttpRequest.getHeader(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_ID)).thenReturn(userIdHeader)
        `when`(mockHttpRequest.getHeader(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.IS_VALIDATED)).thenReturn(isValidatedHeader)
        `when`(mockHttpRequest.getHeader(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_PERMISSIONS)).thenReturn(userPermissionsHeader)

        preAuthorizationFilter.doFilterInternal(mockHttpRequest, mockHttpResponse, mockFilterChain)
        Mockito.verify(mockFilterChain, times(1)).doFilter(mockHttpRequest,mockHttpResponse)
        Assertions.assertEquals(Optional.of(userIdHeader), auditorAwareImpl.currentAuditor)
    }

    @Test
    fun currentAuditorShouldNotExist() {
        val userIdHeader = ""
        val isValidatedHeader = ""
        val userPermissionsHeader = "permission1,permission2"

        `when`(mockHttpRequest.getHeader(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_ID)).thenReturn(userIdHeader)
        `when`(mockHttpRequest.getHeader(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.IS_VALIDATED)).thenReturn(isValidatedHeader)
        `when`(mockHttpRequest.getHeader(com.example.api.spring_boot_kotlin_service.constant.RequestHeaders.USER_PERMISSIONS)).thenReturn(userPermissionsHeader)

        preAuthorizationFilter.doFilterInternal(mockHttpRequest, mockHttpResponse, mockFilterChain)
        Mockito.verify(mockFilterChain, times(1)).doFilter(mockHttpRequest,mockHttpResponse)
        Assertions.assertTrue(auditorAwareImpl.currentAuditor.isEmpty)

    }

    @AfterEach
    fun clearContextAfter() {
        SecurityContextHolder.clearContext()
    }

}