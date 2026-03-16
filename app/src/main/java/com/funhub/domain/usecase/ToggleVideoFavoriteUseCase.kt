package com.funhub.domain.usecase

import com.funhub.domain.repository.VideoRepository
import javax.inject.Inject

class ToggleVideoFavoriteUseCase @Inject constructor(
    private val repository: VideoRepository
) {
    suspend operator fun invoke(videoId: String, isFavorite: Boolean) {
        repository.toggleFavorite(videoId, isFavorite)
    }
}
