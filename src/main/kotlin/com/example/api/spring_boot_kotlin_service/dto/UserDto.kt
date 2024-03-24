package com.example.api.spring_boot_kotlin_service.dto

import com.example.api.spring_boot_kotlin_service.model.Role
import com.example.api.spring_boot_kotlin_service.model.User
import com.example.api.spring_boot_kotlin_service.model.UserRole
import jakarta.validation.constraints.NotBlank

data class UserDto(

    var userId: String = "",

    @field:NotBlank(message = "First Name can not be empty")
    val firstName: String,

    @field:NotBlank(message = "Last Name can not be empty")
    val lastName: String,

    @field:NotBlank(message = "Email can not be empty")
    val email: String,

    @field:NotBlank(message = "Phone can not be empty")
    val phone: String,

    val userRoles: List<UserRoleDto>
){
    fun toUser(userRoles : List<Role>): User{

        return User(
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            phone = this.phone,
        ).apply {
            this.userRoles = userRoles.map {
                UserRole(
                    userId = this.id!!,
                    role = it,
                    userRoleName = it.codeName
                )
            }.toMutableList()
        }
    }
}