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
    val error: String? = null
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

    fun loadImages() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = getImagesUseCase()) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(
                            images = result.data,
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
