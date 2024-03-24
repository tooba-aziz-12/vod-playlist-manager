package com.example.api.vod.security.config

import com.example.api.vod.security.filter.PreAuthorizationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Order(1)
class ApplicationSecurityConfiguration(
    val preAuthorizationFilter: PreAuthorizationFilter
) {
    @Bean
    @Throws(java.lang.Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        return http.cors()
                    .and()
                        .csrf().disable()
                        .authorizeHttpRequests().anyRequest().permitAll()
                    .and()
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                        .exceptionHandling().authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.FORBIDDEN))
                    .and()
                        .addFilterBefore(preAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
                    .build()
    }
}
