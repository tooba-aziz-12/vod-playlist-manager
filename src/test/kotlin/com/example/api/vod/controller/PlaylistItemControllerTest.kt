package com.example.api.vod.controller

import com.example.api.vod.fixture.PlaylistFixture
import com.example.api.vod.fixture.PlaylistFixture.Companion.playListBatchItemDto
import com.example.api.vod.service.PlaylistItemService
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
class PlaylistItemControllerTest {

    private lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapper()

    @Mock
    lateinit var playlistItemService: PlaylistItemService

    companion object {
        const val BASE_URI = "/v1/playlist/item"
    }

    @BeforeEach
    fun setUp(
    ) {
        mockMvc = MockMvcBuilders.standaloneSetup(PlaylistItemController(playlistItemService)).build()
    }


    @Nested
    inner class ReorderPlaylistItemTest{

        @Test
        fun reorderPlaylist(){

            whenever(playlistItemService.reorderItemsInPlaylist(playListBatchItemDto)).thenReturn(
                PlaylistFixture.playlistDto
            )

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.put("${BASE_URI}/reorder")
                    .content(objectMapper.writeValueAsString(playListBatchItemDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()


            Mockito.verify(playlistItemService, times(1)).reorderItemsInPlaylist(playListBatchItemDto)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.OK.value())
        }

        @Test
        fun return400OnBadPayload(){

            val requestDto = playListBatchItemDto.copy()
            requestDto.playlistId = ""

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.put("${BASE_URI}/reorder")
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()


            Mockito.verify(playlistItemService, times(0)).reorderItemsInPlaylist(requestDto)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.BAD_REQUEST.value())
        }

    }

}