package com.example.api.vod.exception

import com.example.api.vod.constant.ErrorCode


class PlaylistNotFoundException(
    playlistId: String,
    errorCode: ErrorCode = ErrorCode.PLAYLIST_NOT_FOUND,
    errorMessage: String = "Playlist with id: $playlistId does not exist") : BaseException(message = errorMessage, errorCode= errorCode.name) {

}
