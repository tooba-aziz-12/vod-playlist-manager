package com.example.api.spring_boot_kotlin_service.model

import com.example.api.spring_boot_kotlin_service.constant.UserRole
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*

@Entity
@Table(name = "role")

data class Role(

    @Column(name = "code_name", nullable = false)
    @Enumerated(EnumType.STRING)
    val codeName: UserRole,

    @Column(name = "description", nullable = false)
    val description: String,

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "role_permission",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id", referencedColumnName = "id")]
    )
    @JsonIgnoreProperties(value = ["roles"])
    var permissions: List<Permission>

) : BaseEntity()