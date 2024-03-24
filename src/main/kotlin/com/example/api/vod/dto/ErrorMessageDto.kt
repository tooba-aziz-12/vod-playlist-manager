package com.example.api.vod.dto

data class ErrorMessageDto(

        val httpStatusCode: String,

        val message: MutableList<String>,

        val errorCode: String
)