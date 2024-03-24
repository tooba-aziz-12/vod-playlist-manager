package com.example.api.vod.service

import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.model.Playlist
import com.example.api.vod.model.extension.convertToDto
import com.example.api.vod.repository.PlaylistRepository
import org.springframework.stereotype.Service


@Service
class PlaylistService(val playlistRepository: PlaylistRepository) {

    fun createPlaylist(name: String): PlaylistDto {
        val playlist = Playlist(name = name)
        return playlistRepository.save(playlist).convertToDto()
    }

    fun getPlaylist(id: Long): PlaylistDto {
        return playlistRepository.findById(id)
            .orElseThrow { NoSuchElementException("Playlist with id $id not found") }
            .convertToDto()
    }

    fun deletePlaylist(id: Long) {
        playlistRepository.deleteById(id)
    }

}
