package com.example.api.vod.service

import com.example.api.vod.dto.PlaylistDto
import com.example.api.vod.exception.FailedToDeletePlaylistException
import com.example.api.vod.exception.FailedToFindPlaylistException
import com.example.api.vod.exception.FailedToSavePlaylistException
import com.example.api.vod.exception.PlaylistNotFoundException
import com.example.api.vod.fixture.PlaylistFixture.Companion.emptyPlaylist
import com.example.api.vod.fixture.PlaylistFixture.Companion.emptyPlaylistDto
import com.example.api.vod.fixture.PlaylistFixture.Companion.playlist
import com.example.api.vod.fixture.PlaylistFixture.Companion.playlistDto
import com.example.api.vod.fixture.PlaylistFixture.Companion.playlistItem2
import com.example.api.vod.fixture.PlaylistFixture.Companion.playlistItem2Dto
import com.example.api.vod.model.Playlist
import com.example.api.vod.model.extension.convertToDto
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
import org.mockito.kotlin.doNothing
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
    inner class UpsertPlaylistTest{

        @Test
        fun createEmptyPlaylist(){

            val requestPlaylist = emptyPlaylist.copy()
            whenever(playlistRepository.save(playlistCaptor.capture())).thenReturn(requestPlaylist)

            val createdPlaylist = playlistService.upsertPlaylist(emptyPlaylistDto)

            Mockito.verify(playlistRepository, times(1)).save(playlistCaptor.capture())

            val actualInvocationOfSave = playlistCaptor.value

            Assertions.assertNotEquals(requestPlaylist.id, "")
            Assertions.assertEquals(requestPlaylist.name, actualInvocationOfSave.name, createdPlaylist.name)

            Assertions.assertEquals(actualInvocationOfSave.items.size, 0)
            Assertions.assertEquals(createdPlaylist.items.size, 0)

        }

        @Test
        fun createPlaylistWithItem(){

            playlistDto.id = ""

            whenever(playlistRepository.save(playlistCaptor.capture())).thenReturn(playlist)

            val createdPlaylist = playlistService.upsertPlaylist(playlistDto)

            Mockito.verify(playlistRepository, times(1)).save(playlistCaptor.capture())

            val actualInvocationOfSave = playlistCaptor.value

            Assertions.assertNotEquals(playlist.id, "")
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
        fun updatePlaylistWithMultipleItems(){

            playlistDto.id = "existing-id-from-db"
            playlistItem2Dto.id = ""
            playlistDto.items.add(playlistItem2Dto)
            playlist.items.add(playlistItem2)
            whenever(playlistRepository.save(playlistCaptor.capture())).thenReturn(playlist)

            val updatedPlaylist = playlistService.upsertPlaylist(playlistDto)

            Mockito.verify(playlistRepository, times(1)).save(playlistCaptor.capture())

            val actualInvocationOfSave = playlistCaptor.value

            Assertions.assertEquals(playlistDto.id, actualInvocationOfSave.id, updatedPlaylist.id)
            Assertions.assertEquals(playlist.name, actualInvocationOfSave.name, updatedPlaylist.name)
            Assertions.assertEquals(playlist.items.size, actualInvocationOfSave.items.size)
            Assertions.assertEquals(playlist.items[0].id, playlistDto.items[0].id)
            Assertions.assertEquals(playlist.items[0].name, actualInvocationOfSave.items[0].name, updatedPlaylist.items[0].name)
            Assertions.assertEquals(playlist.items[0].videoId, actualInvocationOfSave.items[0].videoId, updatedPlaylist.items[0].videoId)
            Assertions.assertEquals(playlist.items[0].sequence, actualInvocationOfSave.items[0].sequence)
            Assertions.assertEquals(playlist.items[0].startTime, actualInvocationOfSave.items[0].startTime)
            Assertions.assertEquals(playlist.items[0].endTime, actualInvocationOfSave.items[0].endTime)
            Assertions.assertEquals(playlistDto.id, actualInvocationOfSave.items[0].playlist.id, updatedPlaylist.id)


            Assertions.assertNotEquals(playlist.items[1].id, "")
            Assertions.assertEquals(playlist.items[1].name, actualInvocationOfSave.items[1].name, updatedPlaylist.items[1].name)
            Assertions.assertEquals(playlist.items[1].videoId, actualInvocationOfSave.items[1].videoId, updatedPlaylist.items[1].videoId)
            Assertions.assertEquals(playlist.items[1].sequence, actualInvocationOfSave.items[1].sequence)
            Assertions.assertEquals(playlist.items[1].startTime, actualInvocationOfSave.items[1].startTime)
            Assertions.assertEquals(playlist.items[1].endTime, actualInvocationOfSave.items[1].endTime)
            Assertions.assertEquals(playlistDto.id, actualInvocationOfSave.items[1].playlist.id, updatedPlaylist.id)

        }

        @Test
        fun exceptionOnSaveFailure(){


            whenever(playlistRepository.save(playlistCaptor.capture())).thenThrow(RuntimeException())

            try {
                playlistService.upsertPlaylist(playlistDto)
            }catch (ex: Exception){
                Assertions.assertEquals(FailedToSavePlaylistException::class.java, ex.javaClass)
            }

            Mockito.verify(playlistRepository, times(1)).save(playlistCaptor.capture())
        }
    }

    @Nested
    inner class GetPlaylistTest{

        @Test
        fun getPlaylistById(){

            playlist.id = "existing-id-from-db"
            val expectedResponse = playlist.convertToDto()

            whenever(playlistRepository.findById(playlist.id!!)).thenReturn(Optional.of(playlist))

            val responsePlaylist = playlistService.getPlaylist(playlist.id!!)

            Assertions.assertEquals(responsePlaylist, expectedResponse)
        }

        @Test
        fun throwCustomExceptionOnFetchFailure(){

            val playlistId = "existing-id-from-db"
            whenever(playlistRepository.findById(playlistId)).thenThrow(RuntimeException::class.java)

            try {

                playlistService.getPlaylist(playlistId)

            }catch (ex: Exception){
                Assertions.assertEquals(FailedToFindPlaylistException::class.java, ex.javaClass)
            }

        }

        @Test
        fun throwCustomExceptionIfNotFound(){

            val playlistId = "wrong id"
            whenever(playlistRepository.findById(playlistId)).thenReturn(Optional.empty())

            try {

                playlistService.getPlaylist(playlistId)

            }catch (ex: Exception){
                Assertions.assertEquals(PlaylistNotFoundException::class.java, ex.javaClass)
            }

        }
    }

    @Nested
    inner class UpdatePlaylistNameTest{
        val requestId = "test-id"
        val playlistName = "new-playlist-name"
        @Test
        fun updatePlaylistName(){

            whenever(playlistRepository.findById(requestId)).thenReturn(Optional.of(playlist))

            whenever(playlistRepository.save(playlistCaptor.capture())).thenReturn(playlist)

            playlistService.updatePlaylistName(requestId, playlistName)

            Mockito.verify(playlistRepository, times(1)).findById(requestId)

            Mockito.verify(playlistRepository, times(1)).save(playlistCaptor.capture())

            val actualInvocationOfSave = playlistCaptor.value

            Assertions.assertEquals(playlistName, actualInvocationOfSave.name)
        }

        @Test
        fun customExceptionIfNotFound(){

            whenever(playlistRepository.findById(requestId)).thenReturn(Optional.empty())

            try {
                playlistService.updatePlaylistName(requestId, playlistName)
            }catch (ex: Exception){
                Assertions.assertEquals(PlaylistNotFoundException::class.java, ex.javaClass)
            }

            Mockito.verify(playlistRepository, times(1)).findById(requestId)

            Mockito.verify(playlistRepository, times(0)).save(playlistCaptor.capture())
        }

        @Test
        fun customExceptionIfSaveFails(){

            whenever(playlistRepository.findById(requestId)).thenReturn(Optional.of(playlist))

            whenever(playlistRepository.save(playlistCaptor.capture())).thenThrow(RuntimeException())

            try {
                playlistService.updatePlaylistName(requestId, playlistName)
            }catch (ex: Exception){
                Assertions.assertEquals(FailedToSavePlaylistException::class.java, ex.javaClass)
            }

            Mockito.verify(playlistRepository, times(1)).findById(requestId)

            Mockito.verify(playlistRepository, times(1)).save(playlistCaptor.capture())
        }

    }

    @Nested
    inner class DeletePlaylistTest{
        val playlistId = "test-playlist-id"
        @Test
        fun updatePlaylistName(){

            whenever(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist))

            doNothing().whenever(playlistRepository.delete(playlistCaptor.capture()))

            playlistService.deletePlaylist(playlistId)

            Mockito.verify(playlistRepository, times(1)).findById(playlistId)

            Mockito.verify(playlistRepository, times(1)).delete(playlistCaptor.capture())

            Mockito.verify(playlistRepository, times(1)).deleteById(playlistId)
        }

        @Test
        fun customExceptionIfNotFound(){

            whenever(playlistRepository.findById(playlistId)).thenReturn(Optional.empty())


            try {
                playlistService.deletePlaylist(playlistId)
            }catch (ex: Exception){
                Assertions.assertEquals(PlaylistNotFoundException::class.java, ex.javaClass)
            }


            Mockito.verify(playlistRepository, times(1)).findById(playlistId)

            Mockito.verify(playlistRepository, times(0)).delete(playlistCaptor.capture())
        }

        @Test
        fun customExceptionIfSaveFails(){

            whenever(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist))

            whenever(playlistRepository.delete(playlistCaptor.capture())).thenThrow(RuntimeException())

            try {
                playlistService.deletePlaylist(playlistId)
            }catch (ex: Exception){
                Assertions.assertEquals(FailedToDeletePlaylistException::class.java, ex.javaClass)
            }

            Mockito.verify(playlistRepository, times(1)).findById(playlistId)

            Mockito.verify(playlistRepository, times(1)).delete(playlistCaptor.capture())
        }

    }

}