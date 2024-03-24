package com.example.api.vod.exception

import com.example.api.vod.constant.ErrorCode


class PlaylistItemNotFoundException(
    playlistItemId: String,
    errorCode: ErrorCode = ErrorCode.PLAYLIST_ITEM_NOT_FOUND,
    errorMessage: String = "Playlist item with id: $playlistItemId does not exist") : BaseException(message = errorMessage, errorCode= errorCode.name) {

}
