package com.example.api.vod.exception

import com.example.api.vod.constant.ErrorCode

class InvalidFieldValueException (
                                  errorCode: ErrorCode = ErrorCode.INVALID_FIELD_VALUE,
                                  errorMessage: String
) : BaseException(message = errorMessage, errorCode= errorCode.name) {

}
