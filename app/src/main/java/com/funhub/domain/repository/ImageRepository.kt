package com.funhub.domain.repository

import com.funhub.domain.model.Image
import com.funhub.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getImages(
        page: Int = 1,
        perPage: Int = 20,
        search: String? = null,
        tagId: Int? = null,
        favorite: Boolean? = null
    ): Result<List<Image>>
    suspend fun getImageById(id: String): Result<Image>
    fun getFavoriteImages(): Flow<List<Image>>
    suspend fun toggleFavorite(imageId: String, isFavorite: Boolean)
}
