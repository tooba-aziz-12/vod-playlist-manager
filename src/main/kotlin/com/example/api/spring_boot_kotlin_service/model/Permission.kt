package com.example.api.spring_boot_kotlin_service.model

import com.fasterxml.jackson.annotation.*
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "permission")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator::class,
    property = "id"
)
data class Permission(

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "permissions")
    @JsonIgnore
    var roles: MutableList<Role> = mutableListOf(),

    @Column(name = "code_name", nullable = false)
    val codeName: String,

    @Column(name = "description", nullable = false)
    val description: String,

    ) : BaseEntity()