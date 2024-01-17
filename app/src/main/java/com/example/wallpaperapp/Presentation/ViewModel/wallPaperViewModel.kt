package com.example.wallpaperapp.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallpaperapp.Presentation.WallPaperUiState
import com.example.wallpaperapp.domain.entity.WallpaperLink
import com.example.wallpaperapp.domain.repository.WallpaperRepository
import com.example.wallpaperempty.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class wallPaperViewModel @Inject constructor(private val repository : WallpaperRepository)  : ViewModel(){
    private val _wallpaperList: MutableStateFlow<WallPaperUiState> =
        MutableStateFlow(WallPaperUiState.Loading)

    val wallpaperList get() = _wallpaperList.asStateFlow()

    fun   fetchWallpapers(){
        viewModelScope.launch(Dispatchers.IO) {
             repository.getImages().collect() {resource->
                 when (resource){
                     is Resource.Success -> {
                         if (resource.data.isNullOrEmpty()){
                             _wallpaperList.update { WallPaperUiState.Loading }

                         }else {
                             _wallpaperList.update { WallPaperUiState.Success(resource.data) }
                         }
                     }
                     is Resource.Error ->{
                         _wallpaperList.update { WallPaperUiState.EmptyList
                     }
                 }




               }
             }
        }




    }

}