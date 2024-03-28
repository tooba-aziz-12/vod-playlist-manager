package com.example.api.vod.service

import com.example.api.vod.fixture.PlaylistFixture
import com.example.api.vod.fixture.PlaylistFixture.Companion.playListReorderItemDto
import com.example.api.vod.fixture.PlaylistFixture.Companion.playlist
import com.example.api.vod.fixture.PlaylistFixture.Companion.playlistItem2
import com.example.api.vod.model.Playlist
import com.example.api.vod.model.PlaylistItem
import com.example.api.vod.repository.PlaylistRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
class PlayListItemServiceTest {

    private lateinit var playlistItemService: PlaylistItemService

    @Mock
    lateinit var playlistRepository: PlaylistRepository


    @Captor
    val playlistCaptor: ArgumentCaptor<Playlist> =
        ArgumentCaptor.forClass(Playlist::class.java)

    @BeforeEach
    fun setup() {
        this.playlistItemService = PlaylistItemService(
            playlistRepository
        )
    }

    @Nested
    inner class ReorderPlaylistItemTest{

        @Test
        fun reorderPlaylist(){

            val requestDto = playListReorderItemDto.copy()

            playListReorderItemDto.items[0].sequence = 2
            playListReorderItemDto.items[1].sequence = 1

            val reorderedPlaylist = playlist.copy()

            reorderedPlaylist.items.add(playlistItem2)

            whenever(playlistRepository.findById(playListReorderItemDto.playlistId)).thenReturn(Optional.of(reorderedPlaylist))

            reorderedPlaylist.items =  playListReorderItemDto.items.map {
                PlaylistItem(
                    reorderedPlaylist,
                    it.videoId,
                    it.startTime,
                    it.endTime,
                    it.name,
                    it.sequence
                ).apply { this@apply.id = it.id }
            }.toMutableList()

            whenever(playlistRepository.save(playlistCaptor.capture())).thenReturn(reorderedPlaylist)

            playlistItemService.reorderItemsInPlaylist(requestDto)

            val actualInvocationOfSave = playlistCaptor.value

            Assertions.assertEquals(2, actualInvocationOfSave.items[0].sequence)
        }
    }

}