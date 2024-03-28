package com.example.api.vod.security.filter

import com.example.api.vod.constant.RequestHeaders
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.stream.Collectors
import java.util.stream.Stream


@Component
class PreAuthorizationFilter : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val userId = request.getHeader(RequestHeaders.USER_ID)
            val permissions = request.getHeader(RequestHeaders.PERMISSIONS).split(",".toRegex())
                .toTypedArray()
            injectSecurityPrincipal(userId, permissions)
            filterChain.doFilter(request, response)
        }catch (ex: Exception){
            return
        }
    }

    private fun injectSecurityPrincipal(userId: String, permissions: Array<String>) {
        // add authenticated principal to current thread's security pool
        if (SecurityContextHolder.getContext().authentication == null) {
            val authorities: List<GrantedAuthority> = Stream.of(*permissions).map { permission: String? ->
                SimpleGrantedAuthority(
                    permission
                )
            }.collect(Collectors.toList())
            val userDetails: UserDetails = User(userId, "", authorities)
            val authentication: Authentication = UsernamePasswordAuthenticationToken(userDetails, null, authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }
    }
}