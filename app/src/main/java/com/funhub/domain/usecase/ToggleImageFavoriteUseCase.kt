package com.funhub.domain.usecase

import com.funhub.domain.repository.ImageRepository
import javax.inject.Inject

class ToggleImageFavoriteUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(imageId: String, isFavorite: Boolean) {
        repository.toggleFavorite(imageId, isFavorite)
    }
}
