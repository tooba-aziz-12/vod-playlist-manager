package com.example.api.vod.repository

import com.example.api.vod.model.PlaylistItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PlaylistItemRepository : JpaRepository<PlaylistItem, String>{

    @Query("Select pi from PlaylistItem pi where pi.id = :id and pi.playlist.id = :playlistId")
    fun findByPlaylistIdAndId(playlistId: String, id: String): Optional<PlaylistItem>
}
