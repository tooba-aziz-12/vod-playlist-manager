package com.example.api.vod.exception

import com.example.api.vod.constant.ErrorCode

class FailedToDeletePlaylistException (
    playlistId: String,
    errorCode: ErrorCode = ErrorCode.PLAYLIST_DELETE_FAILED,
    errorMessage: String = "Failed to delete playlist $playlistId due to some internal server error"
    ) : BaseException(message = errorMessage, errorCode= errorCode.name) {

    }