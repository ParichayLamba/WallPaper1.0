package com.example.wallpaperapp

import com.example.wallpaperapp.Presentation.ViewModel.wallPaperViewModel
import com.example.wallpaperapp.data.api.PickSumApi
import com.example.wallpaperapp.data.api.WallpaperRepositoryImpl
import com.example.wallpaperapp.data.api.model.PicSumItem
import com.example.wallpaperapp.domain.entity.WallpaperLink
import com.example.wallpaperapp.domain.repository.WallpaperRepository
import com.example.wallpaperempty.utils.Resource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class DataManagerTest {
    @Mock
    private lateinit var mockApiService: PickSumApi
    private lateinit var dataRepository: WallpaperRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mockApiService = mock(PickSumApi::class.java)
        dataRepository = WallpaperRepositoryImpl(mockApiService)
    }
    @Test
    fun testGetImagesCase1Success() = runBlocking{
//Case 1 network call is successful
        // then response will have some data
        //  val response = picSumApi.getWallpaperImages()
        val mockPicSumItem = mock(PicSumItem::class.java)
        val mockListPicSumItem = listOf(mockPicSumItem)

        `when`(mockApiService.getWallpaperImages()).thenReturn(mockListPicSumItem)
        val response = dataRepository.getImages().toList()
        val mockWallpaperLinkList: List<WallpaperLink>? = mockListPicSumItem?.map {
            WallpaperLink(it.downloadUrl.orEmpty())
        }
        assertEquals(response.size, mockListPicSumItem.size)
        for (i in 0 until response.size){
            val expectedResource = Resource.Success(mockWallpaperLinkList)
            val actualResource = response[i] as Resource.Success<List<WallpaperLink>>
            assertEquals(expectedResource.javaClass,actualResource.javaClass)
            assertEquals(expectedResource.data,actualResource.data)
        }

    }






}




