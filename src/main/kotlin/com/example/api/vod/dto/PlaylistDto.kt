package com.example.api.vod.dto

import com.example.api.vod.model.Playlist
import com.example.api.vod.model.PlaylistItem
import jakarta.validation.constraints.NotBlank

data class PlaylistDto(
    val id: String = "",

    @field:NotBlank(message = "Name cannot be empty")
    val name: String,

    var items: List<PlaylistItemDto> = mutableListOf()
){
    fun toPlaylist(): Playlist{
        val playlist =  Playlist(
            name = this.name,
        )
        playlist.items = this.items.map {
            PlaylistItem(
                playlist = playlist,
                videoId = it.videoId,
                startTime = it.startTime,
                endTime = it.endTime,
                name = it.name,
                sequence = it.sequence
            )
        }.toMutableList()

        return playlist
    }
}

data class PlaylistItemDto(
    val id: String,
    val playlistId: String,
    val videoId: String,
    val startTime: Long,
    val endTime: Long,
    val name: String,
    val sequence: Long
)

data class PlayListBatchItemDto(

    val playlistId: String,
    val items : List<PlaylistItemDto>
)