package com.example.api.vod.fixture

import com.example.api.vod.dto.PlayListReorderItemDto
import com.example.api.vod.dto.PlaylistItemUpdateDto
import com.example.api.vod.model.Playlist
import com.example.api.vod.model.PlaylistItem
import com.example.api.vod.model.extension.convertToDto
import java.time.LocalDateTime
import java.time.ZoneOffset

class PlaylistFixture {

    companion object{

        private const val playlistName = "test-playlist-name"

        private const val videoId = "test-video-id"

        private val startTime: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)

        private val endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)

        private const val itemName = "test-playlist-item-name"

        private const val itemSequence = 1L


        val emptyPlaylist = Playlist(
            name = playlistName,
        )

        val playlist = Playlist(
            name = playlistName,
            ).apply {
            this@apply.items = mutableListOf(
                PlaylistItem(
                    playlist = this@apply,
                    name = itemName,
                    videoId = videoId,
                    startTime = startTime,
                    endTime = endTime,
                    sequence = itemSequence
                )
            )
        }

        val emptyPlaylistDto = playlist.convertToDto()

        val playlistDto =  playlist.convertToDto()

        val playlistItem = PlaylistItem(
            playlist = playlist,
            name = itemName,
            videoId = videoId,
            startTime = startTime,
            endTime = endTime,
            sequence = itemSequence
        )

        val playlistItem2 = PlaylistItem(
            playlist = playlist,
            name = itemName + "2",
            videoId = videoId+ "2",
            startTime = startTime,
            endTime = endTime,
            sequence = 2L
        )

        val playlistItemDto = playlistItem.convertToDto()

        val playlistItem2Dto = playlistItem2.convertToDto()

        val playListReorderItemDto = PlayListReorderItemDto(
            playlistId = "test-id",
            items = mutableListOf(playlistItemDto, playlistItem2Dto)
        )

        val playListItemUpdateDto = PlaylistItemUpdateDto(
            playlistId = "test-playlist-id",
            playlistItemId = "test-item-id",
            startTime = startTime,
            endTime = endTime,
            name = itemName
        )

    }
}