package com.example.api.spring_boot_kotlin_service.dto

import com.example.api.spring_boot_kotlin_service.constant.UserRole
import jakarta.validation.constraints.NotBlank

data class RoleDto(

    @field:NotBlank(message = "Code Name can not be empty")
    val codeName: UserRole,

    @field:NotBlank(message = "Description can not be empty")
    val description: String,

    var permissionDtos: List<PermissionDto>

)