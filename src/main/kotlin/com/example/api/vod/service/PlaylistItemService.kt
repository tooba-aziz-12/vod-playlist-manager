package com.example.api.vod.service

import com.example.api.vod.dto.PlayListBatchItemDto
import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.dto.PlaylistItemDto
import com.example.api.vod.model.Playlist
import com.example.api.vod.model.PlaylistItem
import com.example.api.vod.model.extension.convertToDto
import com.example.api.vod.repository.PlaylistItemRepository
import com.example.api.vod.repository.PlaylistRepository
import org.springframework.stereotype.Service

@Service
class PlaylistItemService(val playlistRepository: PlaylistRepository) {

    fun addItemToPlaylist(playlistItemDto: PlaylistItemDto): PlaylistDto {
        val playlist = playlistRepository.findById(playlistItemDto.playlistId).get()
        val newItem = PlaylistItem(
            playlist = playlist,
            name = playlistItemDto.name,
            videoId = playlistItemDto.videoId,
            startTime = playlistItemDto.startTime,
            endTime = playlistItemDto.endTime
        )
        playlist.items.add(newItem)
        return playlistRepository.save(playlist).convertToDto()
    }

    fun addItemsToPlaylist(playListBatchItemDto: PlayListBatchItemDto): PlaylistDto {
        val playlist = playlistRepository.findById(playListBatchItemDto.playlistId).get()
        val playlistItems = playListBatchItemDto.items.map {
            PlaylistItem(
                playlist = playlist,
                name = it.name,
                videoId = it.videoId,
                startTime = it.startTime,
                endTime = it.endTime
            )
        }
        playlist.items.addAll(playlistItems)
        return playlistRepository.save(playlist).convertToDto()
    }

    fun reorderItemsInPlaylist(playListBatchItemDto: PlayListBatchItemDto): PlaylistDto {
        val playlist = playlistRepository.findById(playListBatchItemDto.playlistId).get()
        val itemIds = playListBatchItemDto.items.map { it.id }

        val itemMap = playlist.items.associateBy { it.id }

        val reorderedItems = itemIds.mapIndexed { index, itemId ->
            itemMap[itemId]?.copy(sequence = index.toLong()) ?: throw NoSuchElementException("Playlist item with id $itemId not found")
        }

        playlist.items.clear()
        playlist.items.addAll(reorderedItems)

        return playlistRepository.save(playlist).convertToDto()
    }
}
