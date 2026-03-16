package com.funhub.data.repository

import com.funhub.data.local.dao.ImageDao
import com.funhub.data.local.entity.ImageEntity
import com.funhub.data.remote.api.FunHubApi
import com.funhub.data.remote.dto.ImageDto
import com.funhub.domain.model.Image
import com.funhub.domain.model.Result
import com.funhub.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val api: FunHubApi,
    private val imageDao: ImageDao,
    private val serverAddressProvider: ServerAddressProvider
) : ImageRepository {

    override suspend fun getImages(): Result<List<Image>> {
        return try {
            val response = api.getImages()
            if (response.isSuccessful) {
                val images = response.body()?.images?.map { it.toDomainModel(serverAddressProvider.getBaseUrl()) } ?: emptyList()
                // Cache to local database
                imageDao.insertImages(images.map { it.toEntity() })
                Result.Success(images)
            } else {
                Result.Error(HttpException(response), "Failed to load images")
            }
        } catch (e: IOException) {
            // Return cached data on network error
            val cachedImages = imageDao.getAllImages()
            val images = mutableListOf<Image>()
            cachedImages.collect { entities ->
                images.addAll(entities.map { it.toDomainModel() })
            }
            Result.Success(images)
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Unknown error")
        }
    }

    override suspend fun getImageById(id: String): Result<Image> {
        return try {
            val cached = imageDao.getImageById(id)
            if (cached != null) {
                Result.Success(cached.toDomainModel())
            } else {
                Result.Error(Exception("Image not found"), "Image not found")
            }
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Unknown error")
        }
    }

    override fun getFavoriteImages(): Flow<List<Image>> {
        return imageDao.getFavoriteImages().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun toggleFavorite(imageId: String, isFavorite: Boolean) {
        imageDao.updateFavoriteStatus(imageId, isFavorite)
    }

    private fun ImageDto.toDomainModel(baseUrl: String): Image {
        return Image(
            id = id,
            title = title,
            url = baseUrl + url,
            thumbnailUrl = thumbnailUrl?.let { baseUrl + it },
            width = width,
            height = height,
            fileSize = fileSize,
            createdAt = createdAt,
            isFavorite = false
        )
    }

    private fun Image.toEntity(): ImageEntity {
        return ImageEntity(
            id = id,
            title = title,
            url = url,
            thumbnailUrl = thumbnailUrl,
            width = width,
            height = height,
            fileSize = fileSize,
            createdAt = createdAt,
            isFavorite = isFavorite
        )
    }

    private fun ImageEntity.toDomainModel(): Image {
        return Image(
            id = id,
            title = title,
            url = url,
            thumbnailUrl = thumbnailUrl,
            width = width,
            height = height,
            fileSize = fileSize,
            createdAt = createdAt,
            isFavorite = isFavorite
        )
    }
}
