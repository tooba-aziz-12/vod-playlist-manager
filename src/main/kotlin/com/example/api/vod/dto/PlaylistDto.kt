package com.example.api.vod.dto

data class PlaylistDto(
    val id: String,
    val name: String,
    val items: List<PlaylistItemDto>
)

data class PlaylistItemDto(
    val id: String,
    val playlistId: String,
    val videoId: String,
    val startTime: Long,
    val endTime: Long,
    val name: String
)

data class PlayListBatchItemDto(

    val playlistId: String,
    val items : List<PlaylistItemDto>
)