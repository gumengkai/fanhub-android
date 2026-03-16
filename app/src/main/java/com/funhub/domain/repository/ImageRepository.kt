package com.funhub.domain.repository

import com.funhub.domain.model.Image
import com.funhub.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getImages(): Result<List<Image>>
    suspend fun getImageById(id: String): Result<Image>
    fun getFavoriteImages(): Flow<List<Image>>
    suspend fun toggleFavorite(imageId: String, isFavorite: Boolean)
}
