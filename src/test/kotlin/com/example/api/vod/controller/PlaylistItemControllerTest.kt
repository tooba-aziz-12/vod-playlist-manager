package com.example.api.vod.controller

import com.example.api.vod.fixture.PlaylistFixture
import com.example.api.vod.fixture.PlaylistFixture.Companion.deletePlaylistItem
import com.example.api.vod.fixture.PlaylistFixture.Companion.playListItemUpdateDto
import com.example.api.vod.fixture.PlaylistFixture.Companion.playListReorderItemDto
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
        restDocumentation: RestDocumentationContextProvider
    ) {
        mockMvc = MockMvcBuilders.standaloneSetup(PlaylistItemController(playlistItemService))
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
    inner class ReorderPlaylistItemTest{

        @Test
        fun reorderPlaylist(){

            whenever(playlistItemService.reorderItemsInPlaylist(playListReorderItemDto)).thenReturn(
                PlaylistFixture.playlistDto
            )

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.put("${BASE_URI}/reorder")
                    .content(objectMapper.writeValueAsString(playListReorderItemDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()


            Mockito.verify(playlistItemService, times(1)).reorderItemsInPlaylist(playListReorderItemDto)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.OK.value())
        }

        @Test
        fun return400OnBadPayload(){

            val requestDto = playListReorderItemDto.copy()
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

    @Nested
    inner class UpdatePlaylistItemTest{

        @Test
        fun updatePlaylistItem(){

            whenever(playlistItemService.updatePlaylistItem(playListItemUpdateDto)).thenReturn(
                PlaylistFixture.playlistDto
            )

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_URI)
                    .content(objectMapper.writeValueAsString(playListItemUpdateDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()


            Mockito.verify(playlistItemService, times(1)).updatePlaylistItem(playListItemUpdateDto)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.OK.value())
        }

        @Test
        fun return400OnBadPayload(){

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.put(BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()


            Mockito.verify(playlistItemService, times(0)).updatePlaylistItem(playListItemUpdateDto)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.BAD_REQUEST.value())
        }

    }

    @Nested
    inner class DeletePlaylistItemTest{

        @Test
        fun deletePlaylistItem(){

            doNothing().whenever(playlistItemService).deleteItem(deletedItemDto = deletePlaylistItem)

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.delete(BASE_URI)
                    .content(objectMapper.writeValueAsString(deletePlaylistItem))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()


            Mockito.verify(playlistItemService, times(1)).deleteItem(deletePlaylistItem)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.OK.value())
        }

        @Test
        fun return400OnBadPayload(){

            val mvcResult: MvcResult = mockMvc.perform(
                MockMvcRequestBuilders.delete(BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()


            Mockito.verify(playlistItemService, times(0)).deleteItem(deletePlaylistItem)
            Assertions.assertEquals(mvcResult.response.status, HttpStatus.BAD_REQUEST.value())
        }

    }

}