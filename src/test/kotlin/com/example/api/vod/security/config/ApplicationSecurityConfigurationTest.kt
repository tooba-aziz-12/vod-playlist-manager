package com.example.api.vod.security.config

import com.example.api.vod.security.filter.PreAuthorizationFilter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.config.annotation.SecurityBuilder
import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@ExtendWith(MockitoExtension::class)
internal class ApplicationSecurityConfigurationTest {
    private lateinit var securityConfig: ApplicationSecurityConfiguration

    @Mock
    private val preAuthorizationFilter: PreAuthorizationFilter = PreAuthorizationFilter()
    @BeforeEach
    fun beforeEach() {
        securityConfig = ApplicationSecurityConfiguration(
            preAuthorizationFilter
        )
    }

    @Nested
    internal inner class SpringSecurityConfiguration {


        private val httpSecurity : HttpSecurity = Mockito.mock(HttpSecurity::class.java)

        @Mock
        private lateinit var corsConfigurer: CorsConfigurer<HttpSecurity>

        @Mock
        private lateinit var defaultSecurityFilterChain: DefaultSecurityFilterChain

        @Mock
        private lateinit var csrfConfigurer: CsrfConfigurer<HttpSecurity>

        @Mock
        private lateinit var authorizedUrl: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl

        @Mock
        private lateinit var authorizeHttpRequestsConfigurer: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry

        @Mock
        private lateinit var sessionManagementConfigurer: SessionManagementConfigurer<HttpSecurity>

        @Mock
        private lateinit var exceptionHandlingConfigurer: ExceptionHandlingConfigurer<HttpSecurity>
        @Test
        @Throws(Exception::class)
        fun configureHttpSecurity() {
            // CORS should be configured
            Mockito.`when`(httpSecurity.cors()).thenReturn(corsConfigurer)
            Mockito.`when`(corsConfigurer.and()).thenReturn(httpSecurity)
            Mockito.`when`(httpSecurity.build()).thenReturn(defaultSecurityFilterChain)

            // CSRF should be configured to be disabled
            Mockito.`when`(httpSecurity.csrf()).thenReturn(csrfConfigurer)
            Mockito.`when`(csrfConfigurer.disable()).thenReturn(httpSecurity)

            Mockito.`when`(httpSecurity.authorizeHttpRequests()).thenReturn(authorizeHttpRequestsConfigurer)
            Mockito.`when`<Any?>(authorizeHttpRequestsConfigurer.anyRequest()).thenReturn(authorizedUrl)

            Mockito.`when`<AuthorizeHttpRequestsConfigurer<*>.AuthorizationManagerRequestMatcherRegistry>(authorizedUrl.permitAll())
                    .thenReturn(authorizeHttpRequestsConfigurer)

            Mockito.`when`<HttpSecurityBuilder<*>?>(authorizeHttpRequestsConfigurer.and()).thenReturn(httpSecurity)

            Mockito.`when`<HttpSecurityBuilder<*>?>(
                authorizeHttpRequestsConfigurer.and().addFilterBefore(
                    preAuthorizationFilter,UsernamePasswordAuthenticationFilter::class.java
                )
            )
                .thenReturn(httpSecurity)


            // SessionManagement should be configured with STATELESS policy
            Mockito.`when`(httpSecurity.sessionManagement()).thenReturn(sessionManagementConfigurer)
            Mockito.`when`(sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .thenReturn(sessionManagementConfigurer)
            Mockito.`when`<SecurityBuilder<*>?>(sessionManagementConfigurer.and()).thenReturn(httpSecurity)

            // ExceptionHandling should be configured
            Mockito.`when`(httpSecurity.exceptionHandling()).thenReturn(exceptionHandlingConfigurer)
            Mockito.`when`(
                exceptionHandlingConfigurer.authenticationEntryPoint(
                    ArgumentMatchers.any(
                        HttpStatusEntryPoint::class.java
                    )
                )
            ).thenReturn(exceptionHandlingConfigurer)
            Mockito.`when`<SecurityBuilder<*>>(exceptionHandlingConfigurer.and()).thenReturn(httpSecurity)
            //Mockito.`when`<SecurityBuilder<*>>(exceptionHandlingConfigurer.and().addFilterBefore(any(), any())).thenReturn(httpSecurity)
            securityConfig.filterChain(httpSecurity!!)
            Mockito.verify(httpSecurity, Mockito.times(1)).cors()
            Mockito.verify(httpSecurity, Mockito.times(1)).csrf()
            Mockito.verify(csrfConfigurer, Mockito.times(1)).disable()
            Mockito.verify(httpSecurity, Mockito.times(1)).authorizeHttpRequests()
            Mockito.verify<AuthorizeHttpRequestsConfigurer<*>.AuthorizationManagerRequestMatcherRegistry?>(authorizeHttpRequestsConfigurer, Mockito.times(1))
                .anyRequest()
            Mockito.verify(authorizedUrl, Mockito.times(1))
                .permitAll()
            Mockito.verify(httpSecurity, Mockito.times(1)).sessionManagement()
            Mockito.verify(sessionManagementConfigurer, Mockito.times(1))
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            Mockito.verify(httpSecurity, Mockito.times(1)).exceptionHandling()
            Mockito.verify(exceptionHandlingConfigurer, Mockito.times(1)).authenticationEntryPoint(
                ArgumentMatchers.any(
                    HttpStatusEntryPoint::class.java
                )
            )
            Mockito.verify(httpSecurity, Mockito.times(1)).addFilterBefore(
                ArgumentMatchers.same(preAuthorizationFilter), ArgumentMatchers.eq(
                    UsernamePasswordAuthenticationFilter::class.java
                )
            )
        }
    }
}