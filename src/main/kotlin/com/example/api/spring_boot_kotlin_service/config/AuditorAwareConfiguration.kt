package com.example.api.spring_boot_kotlin_service.config

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*


@Component
class AuditorAwareConfiguration : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map { obj: SecurityContext -> obj.authentication }
                .filter { obj: Authentication -> obj.isAuthenticated }
                .map { obj: Authentication -> obj.name }
    }

}