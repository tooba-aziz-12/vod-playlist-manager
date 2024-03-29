package com.example.api.vod.exception

import com.example.api.vod.constant.ErrorCode

class FailedToDeletePlaylistItemException (playlistId: String, playlistItemId: String,
                                           errorCode: ErrorCode = ErrorCode.PLAYLIST_ITEM_DELETE_FAILED,
                                           errorMessage: String = "Failed to Delete playlist item with playlistId: $playlistId and playlistItemId: $playlistItemId due to some internal server error"
) : BaseException(message = errorMessage, errorCode= errorCode.name)