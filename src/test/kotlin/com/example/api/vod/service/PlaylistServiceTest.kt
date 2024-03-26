package com.example.api.vod.service

import com.example.api.vod.exception.FailedToSavePlaylistException
import com.example.api.vod.fixture.PlaylistFixture.Companion.playlist
import com.example.api.vod.fixture.PlaylistFixture.Companion.playlistDto
import com.example.api.vod.model.Playlist
import com.example.api.vod.repository.PlaylistRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
class PlaylistServiceTest {

     private lateinit var playlistService: PlaylistService

     @Mock
     lateinit var playlistRepository: PlaylistRepository


    @Captor
    val playlistCaptor: ArgumentCaptor<Playlist> =
        ArgumentCaptor.forClass(Playlist::class.java)

    @BeforeEach
    fun setup() {
        this.playlistService = PlaylistService(
            playlistRepository
        )
    }

    @Nested
    inner class CreatePlaylistTest{

        @Test
        fun createPlaylist(){


            whenever(playlistRepository.save(playlistCaptor.capture())).thenReturn(playlist)

            val createdPlaylist = playlistService.createPlaylist(playlistDto)

            Mockito.verify(playlistRepository, times(1)).save(playlistCaptor.capture())

            val actualInvocationOfSave = playlistCaptor.value

            Assertions.assertEquals(playlist.name, actualInvocationOfSave.name, createdPlaylist.name)
            Assertions.assertEquals(playlist.name, actualInvocationOfSave.name, createdPlaylist.name)
            Assertions.assertEquals(playlist.items.size, actualInvocationOfSave.items.size)
            Assertions.assertEquals(playlist.items[0].name, actualInvocationOfSave.items[0].name, createdPlaylist.items[0].name)
            Assertions.assertEquals(playlist.items[0].videoId, actualInvocationOfSave.items[0].videoId, createdPlaylist.items[0].videoId)
            Assertions.assertEquals(playlist.items[0].sequence, actualInvocationOfSave.items[0].sequence)
            Assertions.assertEquals(playlist.items[0].startTime, actualInvocationOfSave.items[0].startTime)
            Assertions.assertEquals(playlist.items[0].endTime, actualInvocationOfSave.items[0].endTime)
            Assertions.assertEquals(actualInvocationOfSave.items[0].playlist.id, actualInvocationOfSave.items[0].playlist.id, createdPlaylist.id)

        }

        @Test
        fun exceptionOnSaveFailure(){


            whenever(playlistRepository.save(playlistCaptor.capture())).thenThrow(RuntimeException())

            try {
                playlistService.createPlaylist(playlistDto)
            }catch (ex: Exception){
                Assertions.assertEquals(FailedToSavePlaylistException::class.java, ex.javaClass)
            }

            Mockito.verify(playlistRepository, times(1)).save(playlistCaptor.capture())
        }
    }

}