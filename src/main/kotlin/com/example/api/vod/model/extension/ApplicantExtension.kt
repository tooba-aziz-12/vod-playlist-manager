package com.example.api.vod.model.extension

import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.dto.PlaylistItemDto
import com.example.api.vod.model.Playlist
import com.example.api.vod.model.PlaylistItem

fun Playlist.convertToDto(): PlaylistDto{

    return PlaylistDto(
        id= id!!,
        name = name,
        items = items.map { PlaylistItemDto(
            id = it.id!!,
            name = it.name,
            playlistId = it.playlist.id!!,
            videoId = it.videoId,
            startTime = it.startTime,
            endTime = it.endTime,
            sequence = it.sequence
        ) }.toMutableList()
    )
}

fun PlaylistItem.convertToDto(): PlaylistItemDto{

    return PlaylistItemDto(
        id= id!!,
        name = name,
        playlistId = playlist.id!!,
        videoId = videoId,
        startTime = startTime,
        endTime = endTime,
        sequence = sequence
    )
}
