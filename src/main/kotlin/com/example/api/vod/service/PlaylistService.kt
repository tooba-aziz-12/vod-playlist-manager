package com.example.api.vod.service

import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.dto.PlaylistItemDto
import com.example.api.vod.model.Playlist
import com.example.api.vod.model.extension.convertToDto
import com.example.api.vod.repository.PlaylistRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.NoSuchElementException


@Service
class PlaylistService(val playlistRepository: PlaylistRepository) {

    fun createPlaylist(name: String): PlaylistDto {
        val playlist = Playlist(name = name)
        return savePlaylist(playlist).convertToDto()
    }

    fun getPlaylist(id: String): PlaylistDto {
        return findPlayList(id)
            .orElseThrow { NoSuchElementException("Playlist with id $id not found") }
            .convertToDto()
    }

    fun deletePlaylist(id: String) {
        playlistRepository.deleteById(id)
    }

    fun updatePlaylistName(id: String, newName: String): PlaylistDto {
        val playlist = findPlayList(id).get()
        playlist.name = newName
        return savePlaylist(playlist).convertToDto()
    }

    private fun savePlaylist(playlist: Playlist): Playlist{
       return playlistRepository.save(playlist)
    }

    private fun findPlayList(playlistId: String): Optional<Playlist> {

        return playlistRepository.findById(playlistId)
    }

}
