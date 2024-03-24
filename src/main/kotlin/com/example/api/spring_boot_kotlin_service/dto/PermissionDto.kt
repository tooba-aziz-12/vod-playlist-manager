package com.example.api.spring_boot_kotlin_service.dto

import jakarta.validation.constraints.NotBlank

data class PermissionDto(
    @field:NotBlank(message = "First Name can not be empty")
    val codeName: String,

    @field:NotBlank(message = "First Name can not be empty")
    val description: String,

)