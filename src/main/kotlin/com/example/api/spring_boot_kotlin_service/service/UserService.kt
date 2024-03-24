package com.example.api.spring_boot_kotlin_service.service

import com.example.api.spring_boot_kotlin_service.dto.UserDto
import com.example.api.spring_boot_kotlin_service.dto.UserSearchReqDto
import com.example.api.spring_boot_kotlin_service.exception.UserCreationFailedException
import com.example.api.spring_boot_kotlin_service.exception.UserNotFoundException
import com.example.api.spring_boot_kotlin_service.repository.RoleRepository
import com.example.api.spring_boot_kotlin_service.repository.UserRepository
import org.springframework.stereotype.Service
import toDto

@Service
class UserService (
    val roleRepository: RoleRepository,
    val userRepository: UserRepository
) {

    fun createUser(userDto: UserDto): UserDto{

        try {
            val assignedRoles = userDto.userRoles.map { it.userRoleName }
            val roles = roleRepository.findByCodeNameIn(assignedRoles.map { it.name })
            val user = userDto.toUser(roles)
            val savedUser = userRepository.save(user)
            return savedUser.toDto()
        }catch (ex: Exception){
            throw UserCreationFailedException(errorMessage = "User creation Failed for email : ${userDto.email}")
        }
    }

    fun getUserById(id: String): UserDto {
        try {
            val user = userRepository.findById(id).orElseThrow {
                UserNotFoundException(
                    errorMessage = "User not found for Id: $id"
                )
            }
            return user.toDto()
        }catch (ex: UserNotFoundException){
            throw ex
        }
    }
}