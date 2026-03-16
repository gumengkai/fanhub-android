package com.funhub.ui.images

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funhub.domain.model.Result
import com.funhub.domain.model.Image
import com.funhub.domain.repository.ImageRepository
import com.funhub.domain.usecase.ToggleImageFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ImageDetailUiState(
    val image: Image? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val toggleFavoriteUseCase: ToggleImageFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageDetailUiState())
    val uiState: StateFlow<ImageDetailUiState> = _uiState.asStateFlow()

    fun loadImage(imageId: String) {
        if (_uiState.value.image != null) return // Already loaded
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = imageRepository.getImageById(imageId)) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(
                            image = result.data,
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
        val image = _uiState.value.image ?: return
        
        viewModelScope.launch {
            val newFavoriteStatus = !image.isFavorite
            toggleFavoriteUseCase(image.id, newFavoriteStatus)
            
            _uiState.update { 
                it.copy(image = image.copy(isFavorite = newFavoriteStatus))
            }
        }
    }
}
