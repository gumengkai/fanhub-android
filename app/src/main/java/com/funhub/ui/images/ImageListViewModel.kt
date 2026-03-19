package com.funhub.ui.images

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funhub.domain.model.Result
import com.funhub.domain.model.Image
import com.funhub.domain.usecase.GetImagesUseCase
import com.funhub.domain.usecase.ToggleImageFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ImageListUiState(
    val images: List<Image> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true,
    val searchQuery: String = "",
    val selectedTagId: Int? = null,
    val showFavoritesOnly: Boolean = false
)

@HiltViewModel
class ImageListViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
    private val toggleFavoriteUseCase: ToggleImageFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageListUiState())
    val uiState: StateFlow<ImageListUiState> = _uiState.asStateFlow()

    init {
        loadImages()
    }

    fun loadImages(refresh: Boolean = false) {
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
            val result = getImagesUseCase(
                page = page,
                perPage = 20,
                search = state.searchQuery.takeIf { it.isNotBlank() },
                tagId = state.selectedTagId,
                favorite = if (state.showFavoritesOnly) true else null
            )
            
            when (result) {
                is Result.Success -> {
                    val newImages = if (page == 1) {
                        result.data
                    } else {
                        _uiState.value.images + result.data
                    }
                    
                    _uiState.update { 
                        it.copy(
                            images = newImages,
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

    fun loadMore() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMorePages) return
        _uiState.update { it.copy(currentPage = it.currentPage + 1) }
        loadImages()
    }

    fun refresh() {
        _uiState.update { 
            it.copy(
                currentPage = 1,
                hasMorePages = true
            )
        }
        loadImages(refresh = true)
    }

    fun setSearchQuery(query: String) {
        _uiState.update { 
            it.copy(
                searchQuery = query,
                currentPage = 1,
                hasMorePages = true
            )
        }
        loadImages(refresh = true)
    }

    fun setTagFilter(tagId: Int?) {
        _uiState.update { 
            it.copy(
                selectedTagId = tagId,
                currentPage = 1,
                hasMorePages = true
            )
        }
        loadImages(refresh = true)
    }

    fun toggleFavoritesOnly() {
        _uiState.update { 
            it.copy(
                showFavoritesOnly = !it.showFavoritesOnly,
                currentPage = 1,
                hasMorePages = true
            )
        }
        loadImages(refresh = true)
    }

    fun toggleFavorite(imageId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteUseCase(imageId, isFavorite)
            // Update local state
            _uiState.update { state ->
                state.copy(
                    images = state.images.map { image ->
                        if (image.id == imageId) {
                            image.copy(isFavorite = isFavorite)
                        } else {
                            image
                        }
                    }
                )
            }
        }
    }
}
