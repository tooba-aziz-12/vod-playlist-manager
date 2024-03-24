package com.example.api.vod.controller

import com.example.api.vod.dto.PlayListBatchItemDto
import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.dto.PlaylistItemDto
import com.example.api.vod.service.PlaylistItemService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/playlist/item")
class PlaylistItemController(val playlistItemService: PlaylistItemService) {

    @PostMapping
    fun addItemToPlaylist(
        @RequestBody itemDto: PlaylistItemDto
    ): PlaylistDto {
        return playlistItemService.addItemToPlaylist(
            itemDto
        )
    }

    @PostMapping("/batch")
    fun addItemsToPlaylist(
        @RequestBody itemsDto: PlayListBatchItemDto
    ): PlaylistDto {
        return playlistItemService.addItemsToPlaylist(itemsDto)
    }

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