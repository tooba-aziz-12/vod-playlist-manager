package com.example.api.vod.repository

import com.example.api.vod.model.Playlist
import org.springframework.data.jpa.repository.JpaRepository

interface PlaylistRepository : JpaRepository<Playlist, String>
