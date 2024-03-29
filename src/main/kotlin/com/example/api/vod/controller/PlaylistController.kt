package com.example.api.vod.controller

import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.service.PlaylistService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/playlist")
class PlaylistController(
    val playlistService: PlaylistService
) {

    @PostMapping
    fun upsertPlaylist(
        @RequestBody playlistDto: PlaylistDto,
    ): PlaylistDto {
        return playlistService.upsertPlaylist(playlistDto)

    }

    @GetMapping("/{id}")
    fun getPlaylist(@PathVariable id: String): PlaylistDto {
        return playlistService.getPlaylist(id)
    }

    @PatchMapping("/{id}/name")
    fun updatePlaylistName(
        @PathVariable id: String,
        @RequestParam newName: String
    ): PlaylistDto {
        return playlistService.updatePlaylistName(id, newName)
    }

    @DeleteMapping("/{id}")
    fun deletePlaylist(@PathVariable id: String) {
        playlistService.deletePlaylist(id)
    }
}