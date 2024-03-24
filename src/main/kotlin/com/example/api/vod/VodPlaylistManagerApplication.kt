package com.example.api.vod

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VodPlaylistManagerApplication {

    companion object {
        const val BASE_PACKAGES = "com.example.api.vod"

        @JvmStatic
        fun main(vararg args: String) {
            runApplication<VodPlaylistManagerApplication>(*args)
        }
    }
}
