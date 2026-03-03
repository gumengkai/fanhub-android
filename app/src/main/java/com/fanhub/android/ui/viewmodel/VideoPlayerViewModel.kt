package com.fanhub.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fanhub.android.data.model.Video
import com.fanhub.android.data.repository.FanHubRepository
import com.fanhub.android.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VideoPlayerUiState(
    val video: Video? = null,
    val videoUrl: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val repository: FanHubRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(VideoPlayerUiState())
    val uiState: StateFlow<VideoPlayerUiState> = _uiState.asStateFlow()
    
    fun loadVideo(videoId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            repository.getVideoById(videoId).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        val video = result.data
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                video = video,
                                videoUrl = repository.getVideoStreamUrl(videoId)
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
                }
            }
        }
    }
    
    fun toggleFavorite(videoId: Int) {
        viewModelScope.launch {
            val result = repository.toggleVideoFavorite(videoId)
            if (result is Result.Success) {
                _uiState.update {
                    it.copy(
                        video = it.video?.copy(isFavorite = !(it.video?.isFavorite ?: false))
                    )
                }
            }
        }
    }
}
