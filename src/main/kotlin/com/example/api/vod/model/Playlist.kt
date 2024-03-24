package com.example.api.vod.model

import jakarta.persistence.*

@Entity
@Table(name = "playlist")
data class Playlist(

    @Column(name = "name")
    var name: String,

    @OneToMany(mappedBy = "playlist", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var items: MutableList<PlaylistItem> = mutableListOf()
): BaseEntity()
