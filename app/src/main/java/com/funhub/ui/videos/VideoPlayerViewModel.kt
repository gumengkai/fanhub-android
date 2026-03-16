package com.funhub.ui.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funhub.domain.model.Result
import com.funhub.domain.model.Video
import com.funhub.domain.repository.VideoRepository
import com.funhub.domain.usecase.ToggleVideoFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VideoPlayerUiState(
    val video: Video? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val toggleFavoriteUseCase: ToggleVideoFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VideoPlayerUiState())
    val uiState: StateFlow<VideoPlayerUiState> = _uiState.asStateFlow()

    fun loadVideo(videoId: String) {
        if (_uiState.value.video != null) return // Already loaded
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = videoRepository.getVideoById(videoId)) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(
                            video = result.data,
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun toggleFavorite() {
        val video = _uiState.value.video ?: return
        
        viewModelScope.launch {
            val newFavoriteStatus = !video.isFavorite
            toggleFavoriteUseCase(video.id, newFavoriteStatus)
            
            _uiState.update { 
                it.copy(video = video.copy(isFavorite = newFavoriteStatus))
            }
        }
    }
}
