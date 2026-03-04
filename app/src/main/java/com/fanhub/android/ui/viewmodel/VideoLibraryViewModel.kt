package com.fanhub.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fanhub.android.data.model.Video
import com.fanhub.android.data.repository.FanHubRepository
import com.fanhub.android.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VideoLibraryUiState(
    val videos: Result<List<Video>> = Result.Loading,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class VideoLibraryViewModel @Inject constructor(
    private val repository: FanHubRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VideoLibraryUiState())
    val uiState: StateFlow<VideoLibraryUiState> = _uiState

    private var currentPage = 1
    private val perPage = 20

    init {
        loadVideos()
    }

    fun loadVideos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getVideos(page = currentPage, perPage = perPage)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message,
                        videos = Result.Error(e.message ?: "Unknown error")
                    )
                }
                .collect { videoList ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        videos = Result.Success(videoList)
                    )
                }
        }
    }

    fun searchVideos(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.searchVideos(query)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message,
                        videos = Result.Error(e.message ?: "Unknown error")
                    )
                }
                .collect { videoList ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        videos = Result.Success(videoList)
                    )
                }
        }
    }

    fun toggleFavorite(video: Video) {
        viewModelScope.launch {
            repository.toggleVideoFavorite(video.id)
        }
    }
}
