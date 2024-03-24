package com.example.api.spring_boot_kotlin_service.repository

import com.example.api.spring_boot_kotlin_service.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User,String> {
}