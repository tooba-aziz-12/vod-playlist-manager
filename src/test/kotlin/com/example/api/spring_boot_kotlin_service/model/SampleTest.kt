package com.example.api.spring_boot_kotlin_service.model

import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SampleTest {

    @Test
    fun shouldCreateSampleEntityObject() {
        val user = User()
        Assertions.assertNotNull(user.id)
        Assertions.assertFalse(StringUtils.isBlank(user.id))
        Assertions.assertNotNull(user.createdAt)
        Assertions.assertNotNull(user.updatedAt)
        Assertions.assertEquals("", user.createdBy)
        Assertions.assertEquals("", user.updatedBy)
    }

    @Test
    fun shouldSetId() {
        val user = User()
        user.id = "sample-id-1"

        Assertions.assertEquals("sample-id-1", user.id)
    }
}