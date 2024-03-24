package com.example.api.spring_boot_kotlin_service.dto

data class ErrorMessageDto(

        val httpStatusCode: String,

        val message: MutableList<String>,

        val errorCode: String
)