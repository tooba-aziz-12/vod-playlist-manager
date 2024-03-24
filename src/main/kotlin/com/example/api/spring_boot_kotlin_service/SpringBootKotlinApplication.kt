package com.example.api.spring_boot_kotlin_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootKotlinApplication {

    companion object {
        const val BASE_PACKAGES = "com.example.api.spring_boot_kotlin_service"

        @JvmStatic
        fun main(vararg args: String) {
            runApplication<SpringBootKotlinApplication>(*args)
        }
    }
}
