package com.example.api.vod.service

import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.exception.FailedToDeletePlaylistException
import com.example.api.vod.exception.FailedToFindPlaylistException
import com.example.api.vod.exception.FailedToSavePlaylistException
import com.example.api.vod.exception.PlaylistNotFoundException
import com.example.api.vod.model.Playlist
import com.example.api.vod.model.extension.convertToDto
import com.example.api.vod.repository.PlaylistRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.NoSuchElementException


@Service
class PlaylistService(val playlistRepository: PlaylistRepository) {

    fun createPlaylist(playlistDto: PlaylistDto): PlaylistDto {
        try {
            val playlist = playlistDto.toPlaylist()
            return playlistRepository.save(playlist).convertToDto()
        }catch (ex: Exception){
            throw FailedToSavePlaylistException(playlistName = playlistDto.name)
        }
    }

    fun getPlaylist(id: String): PlaylistDto {
        try {
            return playlistRepository.findById(id)
                .orElseThrow { PlaylistNotFoundException(playlistId = id) }
                .convertToDto()
        }catch (ex: PlaylistNotFoundException){
            throw ex
        }catch (ex: Exception){
            throw FailedToFindPlaylistException(playlistId = id)
        }
    }

    fun deletePlaylist(id: String) {
        try {
            playlistRepository.deleteById(id)
        }catch (ex: Exception){
            throw FailedToDeletePlaylistException(playlistId = id)
        }

    }

    fun updatePlaylistName(id: String, newName: String): PlaylistDto {
        try {
            val playlist = playlistRepository.findById(id).orElseThrow {
                PlaylistNotFoundException(playlistId = id)
            }
            playlist.name = newName
            return playlistRepository.save(playlist).convertToDto()
        }catch (ex: PlaylistNotFoundException){
            throw ex
        }catch (ex: Exception){
            throw FailedToSavePlaylistException(playlistName = newName)
        }
    }
}
