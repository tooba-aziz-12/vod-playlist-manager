package com.example.api.vod.repository

import com.example.api.vod.model.PlaylistItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaylistItemRepository : JpaRepository<PlaylistItem, String>
