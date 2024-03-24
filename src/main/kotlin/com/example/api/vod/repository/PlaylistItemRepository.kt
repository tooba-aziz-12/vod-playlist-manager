package com.example.api.vod.repository

import com.example.api.vod.model.Playlist
import com.example.api.vod.model.PlaylistItem
import org.springframework.data.jpa.repository.JpaRepository

interface PlaylistItemRepository : JpaRepository<PlaylistItem, String>
