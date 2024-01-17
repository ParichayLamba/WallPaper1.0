package com.example.wallpaperapp.domain.repository

import com.example.wallpaperapp.data.api.model.PicSumItem
import com.example.wallpaperempty.utils.Resource
import com.example.wallpaperapp.domain.entity.WallpaperLink as WallpaperLink
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    fun getImages() : Flow<Resource<List<WallpaperLink>>>
}