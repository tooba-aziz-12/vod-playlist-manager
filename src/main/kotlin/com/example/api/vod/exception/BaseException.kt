package com.example.api.vod.exception

import java.lang.RuntimeException

open class BaseException(
        override val message: String,
        val errorCode: String
) : RuntimeException()