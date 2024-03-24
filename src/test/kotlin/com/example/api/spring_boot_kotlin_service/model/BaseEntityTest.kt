package com.example.api.spring_boot_kotlin_service.model

import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class BaseEntityTest {

    @Test
    fun shouldCreateBaseEntityWithFriendlyId() {
        val baseEntity = com.example.api.spring_boot_kotlin_service.model.BaseEntity()
        Assertions.assertNotNull(baseEntity.id)
        Assertions.assertFalse(StringUtils.isBlank(baseEntity.id))
        Assertions.assertNotNull(baseEntity.createdAt)
        Assertions.assertNotNull(baseEntity.updatedAt)
        Assertions.assertEquals("", baseEntity.createdBy)
        Assertions.assertEquals("", baseEntity.updatedBy)
    }

    @Test
    fun shouldCreateBaseEntityWithTheIdProvided() {
        val baseEntity = com.example.api.spring_boot_kotlin_service.model.BaseEntity("auth-id-1")

        Assertions.assertEquals("auth-id-1", baseEntity.id)
    }

    @Test
    fun shouldUpdateBaseEntityWithSetters(){
        val baseEntity = com.example.api.spring_boot_kotlin_service.model.BaseEntity("auth-id-1")
        baseEntity.createdBy = "test-user"
        baseEntity.updatedBy = "test-user"
        baseEntity.createdAt = LocalDateTime.now()
        baseEntity.updatedAt = LocalDateTime.now()
        Assertions.assertNotNull(baseEntity.createdBy)
        Assertions.assertNotNull(baseEntity.updatedBy)
        Assertions.assertNotNull(baseEntity.createdAt)
        Assertions.assertNotNull(baseEntity.updatedAt)
    }

}