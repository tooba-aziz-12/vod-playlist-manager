package com.example.api.vod.controller

import com.example.api.vod.dto.*
import com.example.api.vod.service.PlaylistItemService
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/playlist/item")
@Validated
class PlaylistItemController(val playlistItemService: PlaylistItemService) {

    @PutMapping("/reorder")
    fun reorderItemsInPlaylist(
        @RequestBody @Valid itemsDto: PlayListReorderItemDto
    ): PlaylistDto {
        return playlistItemService.reorderItemsInPlaylist(itemsDto)
    }

    @PutMapping
    fun updatePlaylistItem(
        @RequestBody updatedItemDto: PlaylistItemUpdateDto
    ): PlaylistDto {
        return playlistItemService.updatePlaylistItem(updatedItemDto)
    }

    @DeleteMapping
    fun deletePlaylistItem(
        @RequestBody deletedItemDto: PlaylistItemDeleteDto
    ) {
        return playlistItemService.deleteItem(deletedItemDto)
    }
}