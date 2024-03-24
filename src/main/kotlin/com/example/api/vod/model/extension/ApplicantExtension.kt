package com.example.api.vod.model.extension

import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.dto.PlaylistItemDto
import com.example.api.vod.model.Playlist

fun Playlist.convertToDto(): PlaylistDto{

    return PlaylistDto(
        id= id!!,
        name = name,
        items = items.map { PlaylistItemDto(
            id = it.id!!,
            videoId = it.videoId,
            startTime = it.startTime,
            endTime = it.endTime
        ) }
    )
}
