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
    fun getPlaylist(@PathVariable id: Long): PlaylistDto {
        return playlistService.getPlaylist(id)
    }

    @DeleteMapping("/{id}")
    fun deletePlaylist(@PathVariable id: Long) {
        playlistService.deletePlaylist(id)
    }
}