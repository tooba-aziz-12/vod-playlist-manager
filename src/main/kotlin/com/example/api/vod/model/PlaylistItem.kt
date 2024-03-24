package com.example.api.vod.model

import jakarta.persistence.*

@Entity
@Table(name = "playlist_item")
data class PlaylistItem(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    var playlist: Playlist,

    @Column(name = "video_id")
    var videoId: String,

    @Column(name = "start_time")
    var startTime: Long,

    @Column(name = "end_time")
    var endTime: Long,

    @Column(name = "name")
    var name: String,

    @Column(name = "sequence")
    var sequence: Long = 0L
): BaseEntity()

