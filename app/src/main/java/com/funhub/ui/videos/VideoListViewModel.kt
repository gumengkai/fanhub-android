package com.funhub.ui.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funhub.domain.model.Result
import com.funhub.domain.model.Video
import com.funhub.domain.usecase.GetVideosUseCase
import com.funhub.domain.usecase.ToggleVideoFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VideoListUiState(
    val videos: List<Video> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGridView: Boolean = true
)

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase,
    private val toggleFavoriteUseCase: ToggleVideoFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VideoListUiState())
    val uiState: StateFlow<VideoListUiState> = _uiState.asStateFlow()

    init {
        loadVideos()
    }

    fun loadVideos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = getVideosUseCase()) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(
                            videos = result.data,
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

    fun toggleViewMode() {
        _uiState.update { it.copy(isGridView = !it.isGridView) }
    }

    fun toggleFavorite(videoId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteUseCase(videoId, isFavorite)
            // Update local state
            _uiState.update { state ->
                state.copy(
                    videos = state.videos.map { video ->
                        if (video.id == videoId) {
                            video.copy(isFavorite = isFavorite)
                        } else {
                            video
                        }
                    }
                )
            }
        }
    }
}
