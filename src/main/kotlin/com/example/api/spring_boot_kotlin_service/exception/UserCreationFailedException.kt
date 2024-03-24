package com.example.api.spring_boot_kotlin_service.exception

import com.example.api.spring_boot_kotlin_service.constant.ErrorCode

class UserCreationFailedException(errorCode: ErrorCode = ErrorCode.USER_CREATION_FAILED, errorMessage: String) : BaseException(message = errorMessage, errorCode = errorCode.name ){

}