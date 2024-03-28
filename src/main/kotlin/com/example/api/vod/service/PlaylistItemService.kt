package com.example.api.vod.service

import com.example.api.vod.dto.PlayListReorderItemDto
import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.dto.PlaylistItemDto
import com.example.api.vod.dto.PlaylistItemUpdateDto
import com.example.api.vod.exception.FailedToSavePlaylistItemException
import com.example.api.vod.exception.InvalidFieldValueException
import com.example.api.vod.exception.PlaylistItemNotFoundException
import com.example.api.vod.exception.PlaylistNotFoundException
import com.example.api.vod.model.extension.convertToDto
import com.example.api.vod.repository.PlaylistRepository
import org.springframework.stereotype.Service

@Service
class PlaylistItemService(val playlistRepository: PlaylistRepository) {

    fun reorderItemsInPlaylist(playListReorderItemDto: PlayListReorderItemDto): PlaylistDto {
        try {
            val playlistFromDb = playlistRepository.findById(playListReorderItemDto.playlistId).orElseThrow {
                PlaylistNotFoundException(playlistId = playListReorderItemDto.playlistId)
            }
            val itemIdAgainstSequence : MutableMap<String, Long> = mutableMapOf()

            playListReorderItemDto.items.map {
                itemIdAgainstSequence[it.id] = it.sequence
            }

            playlistFromDb.items.forEach { item->
                val updatedSequence  = itemIdAgainstSequence[item.id]?: throw PlaylistItemNotFoundException("Playlist item with id ${item.id} not found")
                item.sequence = updatedSequence
            }

            return playlistRepository.save(playlistFromDb).convertToDto()
        }catch (ex: PlaylistNotFoundException){
            throw ex
        }catch (ex: PlaylistItemNotFoundException){
            throw ex
        } catch (ex: Exception){
            throw FailedToSavePlaylistItemException(playlistId = playListReorderItemDto.playlistId)
        }
    }

    fun updatePlaylistItem(updatedItemDto: PlaylistItemUpdateDto): PlaylistDto {
        try {
            if (updatedItemDto.startTime >= updatedItemDto.endTime){
                throw InvalidFieldValueException(errorMessage = "Start time can not be less than end time")
            }
            val playlist = playlistRepository.findById(updatedItemDto.playlistId).orElseThrow {
                PlaylistNotFoundException(playlistId = updatedItemDto.playlistId)
            }
            val item = playlist.items.find { it.id == updatedItemDto.playlistItemId }
                ?: throw PlaylistItemNotFoundException("Playlist item with id $updatedItemDto.id not found")

            item.apply {
                name = updatedItemDto.name
                startTime = updatedItemDto.startTime
                endTime = updatedItemDto.endTime
            }

            return playlistRepository.save(playlist).convertToDto()
        }catch (ex: InvalidFieldValueException){
            throw ex
        }catch (ex: PlaylistNotFoundException){
            throw ex
        }catch (ex: PlaylistItemNotFoundException){
            throw ex
        } catch (ex: Exception){
            throw FailedToSavePlaylistItemException(playlistId = updatedItemDto.playlistId)
        }

    }
}
