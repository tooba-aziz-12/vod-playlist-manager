package com.example.api.vod.controller

import com.example.api.vod.fixture.PlaylistFixture.Companion.playlistDto
import com.example.api.vod.service.PlaylistService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@ExtendWith(
    *arrayOf(
        MockitoExtension::class,
        RestDocumentationExtension::class
    )
)
class PlaylistControllerTest {

    private lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapper()

    @Mock
    lateinit var playlistService: PlaylistService

    companion object {
        const val BASE_URI = "/v1/playlist"
    }

    @BeforeEach
    fun setUp(
        restDocumentation: RestDocumentationContextProvider
    ) {
        mockMvc = MockMvcBuilders.standaloneSetup(PlaylistController(playlistService))
            .alwaysDo<StandaloneMockMvcBuilder>(
                MockMvcRestDocumentation.document(
                    "{class-name}/{method-name}",
                    Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                    Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
                )
            )
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)).build()

    }

    @Nested
    inner class UpsertPlaylistTest{

        @Test
        fun upsertPlaylist(){

            whenever(playlistService.upsertPlaylist(playlistDto)).thenReturn(
                playlistDto
            )

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URI)
                    .content(objectMapper.writeValueAsString(playlistDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()


            Mockito.verify(playlistService, times(1)).upsertPlaylist(playlistDto)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.OK.value())
        }

        @Test
        fun shouldReturn400OnBadPayload(){

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()


            Mockito.verify(playlistService, times(0)).upsertPlaylist(any())
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.BAD_REQUEST.value())

        }
    }

    @Nested
    inner class GetPlaylistTest{
        val requestId = "test-id"

        @Test
        fun getPlaylistById(){



            whenever(playlistService.getPlaylist(requestId)).thenReturn(
                playlistDto
            )

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("$BASE_URI/$requestId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()


            Mockito.verify(playlistService, times(1)).getPlaylist(requestId)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.OK.value())
        }

        @Test
        fun shouldReturn405OnEmptyPathVar(){

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed)
                .andReturn()


            Mockito.verify(playlistService, times(0)).upsertPlaylist(playlistDto)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.METHOD_NOT_ALLOWED.value())

        }
    }

    @Nested
    inner class UpdatePlaylistNameTest{
        val requestId = "test-id"
        val playlistName = "new-playlist-name"

        @Test
        fun updatePlaylistName(){


            whenever(playlistService.updatePlaylistName(requestId, playlistName)).thenReturn(
                playlistDto
            )

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.patch("$BASE_URI/$requestId/name")
                    .param("newName", playlistName)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()


            Mockito.verify(playlistService, times(1)).updatePlaylistName(requestId, playlistName)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.OK.value())
        }

        @Test
        fun shouldReturn405OnEmptyPathVar(){

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.patch(BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed)
                .andReturn()


            Mockito.verify(playlistService, times(0)).updatePlaylistName(any(), any())
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.METHOD_NOT_ALLOWED.value())

        }
    }


    @Nested
    inner class DeletePlaylistTest{
        private val playlistId = "test-id"

        @Test
        fun deletePlaylist(){


            doNothing().whenever(playlistService).deletePlaylist(playlistId)

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.delete("$BASE_URI/$playlistId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()


            Mockito.verify(playlistService, times(1)).deletePlaylist(playlistId)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.OK.value())
        }

        @Test
        fun shouldReturn405OnEmptyPathVar(){

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.delete(BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed)
                .andReturn()


            Mockito.verify(playlistService, times(0)).deletePlaylist(any())
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.METHOD_NOT_ALLOWED.value())

        }
    }
}