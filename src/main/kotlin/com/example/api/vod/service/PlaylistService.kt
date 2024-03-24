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

    fun createPlaylist(name: String): PlaylistDto {
        val playlist = Playlist(name = name)
        return savePlaylist(playlist).convertToDto()
    }

    fun getPlaylist(id: String): PlaylistDto {
        try {
            return findPlayList(id)
                .orElseThrow { PlaylistNotFoundException(playlistId = id) }
                .convertToDto()
        }catch (ex: PlaylistNotFoundException){
            throw ex
        }catch (ex: FailedToFindPlaylistException){
            throw ex
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
        val playlist = findPlayList(id).get()
        playlist.name = newName
        return savePlaylist(playlist).convertToDto()
    }

    private fun savePlaylist(playlist: Playlist): Playlist{
        try {
            return playlistRepository.save(playlist)
        }catch (ex: Exception){
            throw FailedToSavePlaylistException(playlistName = playlist.name)
        }
    }

    private fun findPlayList(playlistId: String): Optional<Playlist> {

        try {
            return playlistRepository.findById(playlistId)
        }catch (ex: Exception){
            throw FailedToFindPlaylistException(playlistId = playlistId)
        }
    }

}
