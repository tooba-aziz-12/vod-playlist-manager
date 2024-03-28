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
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

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
    ) {
        mockMvc = MockMvcBuilders.standaloneSetup(PlaylistController(playlistService)).build()
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


            Mockito.verify(playlistService, times(0)).upsertPlaylist(playlistDto)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.BAD_REQUEST.value())

        }
    }
}