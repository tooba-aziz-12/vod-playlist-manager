package com.example.api.vod.controller

import com.example.api.vod.constant.RequestHeaders
import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.service.PlaylistService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/playlist")
class PlaylistController(
    val playlistService: PlaylistService
) {

    @PostMapping()
    fun createPlaylist(
        @RequestParam name: String,
        @RequestHeader(RequestHeaders.USER_ID) userId: String,
    ): PlaylistDto {
        return playlistService.createPlaylist(name)

    }

    @GetMapping("/{id}")
    fun getPlaylist(@PathVariable id: String): PlaylistDto {
        return playlistService.getPlaylist(id)
    }

    @DeleteMapping("/{id}")
    fun deletePlaylist(@PathVariable id: String) {
        playlistService.deletePlaylist(id)
    }

    @PutMapping("/{id}/name")
    fun updatePlaylistName(
        @PathVariable id: String,
        @RequestBody newName: String
    ): PlaylistDto {
        return playlistService.updatePlaylistName(id, newName)
    }
}