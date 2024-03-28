package com.example.api.vod.controller

import com.example.api.vod.dto.PlayListBatchItemDto
import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.dto.PlaylistItemDto
import com.example.api.vod.service.PlaylistItemService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/playlist/item")
class PlaylistItemController(val playlistItemService: PlaylistItemService) {

    @PutMapping("/reorder")
    fun reorderItemsInPlaylist(
        @RequestBody itemsDto: PlayListBatchItemDto
    ): PlaylistDto {
        return playlistItemService.reorderItemsInPlaylist(itemsDto)
    }

    @PutMapping
    fun updatePlaylistItem(
        @RequestBody updatedItemDto: PlaylistItemDto
    ): PlaylistDto {
        return playlistItemService.updatePlaylistItem(updatedItemDto)
    }
}