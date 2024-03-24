package com.example.api.spring_boot_kotlin_service.model

import com.example.api.spring_boot_kotlin_service.constant.UserRole
import jakarta.persistence.*

@Entity
@Table(name = "user_role")
data class UserRole(
    @Column(name = "user_id", nullable = false)
    val userId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    val role: Role,

    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    val userRoleName: UserRole,

    ) : BaseEntity()
