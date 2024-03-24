package com.example.api.vod.exception

import com.example.api.vod.constant.ErrorCode

class FailedToSavePlaylistItemException(
    playlistId: String,
    errorCode: ErrorCode = ErrorCode.PLAYLIST_ITEM_SAVE_FAILED,
    errorMessage: String = "Failed to save playlist item for playlist id $playlistId due to some internal server error"
) : BaseException(message = errorMessage, errorCode= errorCode.name) {

}
