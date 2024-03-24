package com.example.api.vod.exception

import com.example.api.vod.constant.ErrorCode

class FailedToFindPlaylistException(
    playlistId: String,
    errorCode: ErrorCode = ErrorCode.PLAYLIST_SEARCH_FAILED,
    errorMessage: String = "Failed to find playlist with id: $playlistId due to some internal server error"
) : BaseException(message = errorMessage, errorCode= errorCode.name) {

}
