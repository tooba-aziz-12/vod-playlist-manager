package com.example.api.vod.service

import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.exception.*
import com.example.api.vod.model.extension.convertToDto
import com.example.api.vod.repository.PlaylistRepository
import org.springframework.stereotype.Service


@Service
class PlaylistService(val playlistRepository: PlaylistRepository) {

    fun upsertPlaylist(playlistDto: PlaylistDto): PlaylistDto {
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

    fun deletePlaylist(id: String) {
        try {
            val playlist  = playlistRepository.findById(id)
                .orElseThrow { PlaylistNotFoundException(id) }
            playlistRepository.delete(playlist)
        }catch (ex: PlaylistNotFoundException){
            throw ex
        }catch (ex: Exception){
            throw FailedToDeletePlaylistException(playlistId = id)
        }

    }
}
