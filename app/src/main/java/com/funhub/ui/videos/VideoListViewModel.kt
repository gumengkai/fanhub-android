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
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val isGridView: Boolean = true,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true,
    val searchQuery: String = "",
    val selectedTagId: Int? = null,
    val showFavoritesOnly: Boolean = false,
    val sortBy: String = "created_at",
    val sortOrder: String = "desc"
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

    fun loadVideos(refresh: Boolean = false) {
        viewModelScope.launch {
            val page = if (refresh) 1 else _uiState.value.currentPage
            
            _uiState.update { 
                it.copy(
                    isLoading = page == 1,
                    isLoadingMore = page > 1,
                    error = null
                )
            }
            
            val state = _uiState.value
            val result = getVideosUseCase(
                page = page,
                perPage = 20,
                search = state.searchQuery.takeIf { it.isNotBlank() },
                tagId = state.selectedTagId,
                favorite = if (state.showFavoritesOnly) true else null,
                sortBy = state.sortBy,
                order = state.sortOrder
            )
            
            when (result) {
                is Result.Success -> {
                    val newVideos = if (page == 1) {
                        result.data
                    } else {
                        _uiState.value.videos + result.data
                    }
                    
                    _uiState.update { 
                        it.copy(
                            videos = newVideos,
                            isLoading = false,
                            isLoadingMore = false,
                            currentPage = page,
                            hasMorePages = result.data.size >= 20
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            error = result.message
                        )
                    }
                }
                is Result.Loading -> {
                    // Already handled
                }
            }
        }
    }

    fun goToPage(page: Int) {
        if (page < 1 || _uiState.value.isLoading) return
        _uiState.update { it.copy(currentPage = page) }
        loadVideos(refresh = true)
    }

    fun refresh() {
        _uiState.update { 
            it.copy(
                currentPage = 1,
                hasMorePages = true
            )
        }
        loadVideos(refresh = true)
    }

    fun setSearchQuery(query: String) {
        _uiState.update { 
            it.copy(
                searchQuery = query,
                currentPage = 1,
                hasMorePages = true
            )
        }
        loadVideos(refresh = true)
    }

    fun setTagFilter(tagId: Int?) {
        _uiState.update { 
            it.copy(
                selectedTagId = tagId,
                currentPage = 1,
                hasMorePages = true
            )
        }
        loadVideos(refresh = true)
    }

    fun toggleFavoritesOnly() {
        _uiState.update { 
            it.copy(
                showFavoritesOnly = !it.showFavoritesOnly,
                currentPage = 1,
                hasMorePages = true
            )
        }
        loadVideos(refresh = true)
    }

    fun setSortBy(sortBy: String, order: String) {
        _uiState.update { 
            it.copy(
                sortBy = sortBy,
                sortOrder = order,
                currentPage = 1,
                hasMorePages = true
            )
        }
        loadVideos(refresh = true)
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
