package com.fanhub.android.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fanhub.android.data.model.ImageListResponse
import com.fanhub.android.data.repository.FanHubRepository
import com.fanhub.android.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ImageLibraryUiState(
    val images: Result<ImageListResponse> = Result.Loading
)

@HiltViewModel
class ImageLibraryViewModel @Inject constructor(
    private val repository: FanHubRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ImageLibraryUiState())
    val uiState: StateFlow<ImageLibraryUiState> = _uiState.asStateFlow()
    
    var isFavoriteFilter by mutableStateOf(false)
        private set
    
    init {
        loadImages()
    }
    
    fun loadImages() {
        viewModelScope.launch {
            repository.getImages(
                page = 1,
                perPage = 50,
                favorite = if (isFavoriteFilter) true else null
            ).collect { result ->
                _uiState.update { it.copy(images = result) }
            }
        }
    }
    
    fun toggleFavoriteFilter() {
        isFavoriteFilter = !isFavoriteFilter
        loadImages()
    }
    
    fun toggleFavorite(imageId: Int) {
        viewModelScope.launch {
            repository.toggleImageFavorite(imageId)
            loadImages() // 重新加载以更新状态
        }
    }
}
