package com.funhub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funhub.domain.model.Image
import com.funhub.domain.model.Result
import com.funhub.domain.model.Video
import com.funhub.domain.repository.ImageRepository
import com.funhub.domain.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val videos: List<Video> = emptyList(),
    val images: List<Image> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadContent()
    }

    fun loadContent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            // Load videos
            when (val videoResult = videoRepository.getVideos()) {
                is Result.Success -> {
                    _uiState.update { state ->
                        state.copy(videos = videoResult.data)
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(error = videoResult.message) }
                }
                else -> {}
            }
            
            // Load images
            when (val imageResult = imageRepository.getImages()) {
                is Result.Success -> {
                    _uiState.update { state ->
                        state.copy(images = imageResult.data, isLoading = false)
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(error = imageResult.message, isLoading = false) }
                }
                else -> {}
            }
        }
    }

    fun toggleVideoFavorite(videoId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            videoRepository.toggleFavorite(videoId, isFavorite)
            _uiState.update { state ->
                state.copy(
                    videos = state.videos.map { video ->
                        if (video.id == videoId) video.copy(isFavorite = isFavorite) else video
                    }
                )
            }
        }
    }

    fun toggleImageFavorite(imageId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            imageRepository.toggleFavorite(imageId, isFavorite)
            _uiState.update { state ->
                state.copy(
                    images = state.images.map { image ->
                        if (image.id == imageId) image.copy(isFavorite = isFavorite) else image
                    }
                )
            }
        }
    }
}
