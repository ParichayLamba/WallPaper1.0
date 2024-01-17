package com.example.wallpaperapp.data.api
import com.example.wallpaperapp.Utils.Constants.END_POINT
import com.example.wallpaperapp.data.api.model.PicSumItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//RETROFIT SHIT
interface PickSumApi {

    @GET(END_POINT)
   suspend fun getWallpaperImages(
        @Query("page") page: Int = 1, @Query("limit") limit: Int = 100
    ): List<PicSumItem>?
}