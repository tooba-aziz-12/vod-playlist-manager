package com.example.api.spring_boot_kotlin_service.dto

import com.example.api.spring_boot_kotlin_service.constant.UserRole
import jakarta.validation.constraints.NotBlank

data class UserRoleDto(
    val userRoleId: String = "",

    val userRoleName: UserRole,
)