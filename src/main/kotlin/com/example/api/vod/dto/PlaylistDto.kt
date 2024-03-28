package com.example.api.vod.dto

import com.example.api.vod.model.Playlist
import com.example.api.vod.model.PlaylistItem
import jakarta.validation.constraints.NotBlank

data class PlaylistDto(
    var id: String = "",

    @field:NotBlank(message = "Name cannot be empty")
    val name: String,

    var items: MutableList<PlaylistItemDto> = mutableListOf()
){
    fun toPlaylist(): Playlist{
        val playlist =  Playlist(
            name = this.name,
        )
        playlist.items = this.items.map {
            val item = PlaylistItem(
                playlist = playlist,
                videoId = it.videoId,
                startTime = it.startTime,
                endTime = it.endTime,
                name = it.name,
                sequence = it.sequence
            )
            if (it.id.isNotBlank()){
                item.id = it.id
            }
            item
        }.toMutableList()

        if (id.isNotBlank()){
            playlist.id = id
        }

        return playlist
    }
}

data class PlaylistItemDto(
    var id: String = "",
    @field:NotBlank(message = "Name cannot be empty")
    val playlistId: String,
    @field:NotBlank(message = "Name cannot be empty")
    val videoId: String,
    val startTime: Long,
    val endTime: Long,
    @field:NotBlank(message = "Name cannot be empty")
    var name: String,
    var sequence: Long
)

data class PlayListReorderItemDto(

    @field:NotBlank(message = "Name cannot be empty")
    var playlistId: String,
    val items : List<PlaylistItemDto>
)

data class PlaylistItemUpdateDto(
    @field:NotBlank(message = "Playlist id cannot be empty")
    var playlistId: String,

    @field:NotBlank(message = "Item id cannot be empty")
    var playlistItemId: String,

    @field:NotBlank(message = "Name cannot be empty")
    var name: String,

    var startTime: Long,

    val endTime: Long,
)