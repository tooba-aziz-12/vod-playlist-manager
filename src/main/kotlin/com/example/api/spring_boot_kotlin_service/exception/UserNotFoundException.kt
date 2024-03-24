package com.example.api.spring_boot_kotlin_service.exception

import com.example.api.spring_boot_kotlin_service.constant.ErrorCode

class UserNotFoundException(errorCode: ErrorCode = ErrorCode.USER_NOT_FOUND, errorMessage: String) : BaseException(message = errorMessage, errorCode = errorCode.name ) {

}