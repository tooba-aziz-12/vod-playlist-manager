package com.example.api.vod.exception

import com.example.api.vod.constant.ErrorCode

class FailedToSavePlaylistException(
    playlistName: String,
    errorCode: ErrorCode = ErrorCode.PLAYLIST_SAVE_FAILED,
    errorMessage: String = "Failed to save playlist $playlistName due to some internal server error"
) : BaseException(message = errorMessage, errorCode= errorCode.name) {

}
