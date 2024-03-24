package com.example.api.spring_boot_kotlin_service.exception

import java.lang.RuntimeException

open class BaseException(
        override val message: String,
        val errorCode: String
) : RuntimeException()