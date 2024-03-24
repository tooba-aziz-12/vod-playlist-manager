package com.example.api.spring_boot_kotlin_service.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*

@Entity
@Table(name = "user")
data class User(

    @Column(name = "first_name", nullable = false)
    val firstName: String = "",

    @Column(name = "last_name", nullable = false)
    val lastName: String = "",

    @Column(name = "email", nullable = false)
    val email: String = "",

    @Column(name = "password", nullable = false)
    var password: String = "",

    @Column(name = "phone", nullable = true)
    val phone: String = "",

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], mappedBy = "userId")
    @JsonIgnoreProperties(value = ["role"])
    var userRoles: MutableList<UserRole> = mutableListOf()

): BaseEntity()