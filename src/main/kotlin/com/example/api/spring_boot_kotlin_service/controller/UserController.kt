package com.example.api.spring_boot_kotlin_service.controller

import com.example.api.spring_boot_kotlin_service.dto.UserDto
import com.example.api.spring_boot_kotlin_service.dto.UserSearchReqDto
import com.example.api.spring_boot_kotlin_service.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
@Validated
class UserController (
    val userService: UserService
){
    @PreAuthorize("hasRole('ROLE_1')")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun createUser(
        @RequestBody @Valid userDto: UserDto
    ): UserDto {
        return userService.createUser(userDto)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getUser(
        @PathVariable("id") id: String
    ): UserDto {
        return userService.getUserById(id)
    }
}
