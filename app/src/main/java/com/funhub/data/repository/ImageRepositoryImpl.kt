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

    override suspend fun getImages(
        page: Int,
        perPage: Int,
        search: String?,
        tagId: Int?,
        favorite: Boolean?
    ): Result<List<Image>> {
        return try {
            val response = api.getImages(
                page = page,
                perPage = perPage,
                search = search,
                tagId = tagId,
                favorite = favorite
            )
            if (response.isSuccessful) {
                val images = response.body()?.items?.map { 
                    convertImageDtoToDomain(it, serverAddressProvider.getBaseUrl()) 
                } ?: emptyList()
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
            // Try to get from API first
            val response = api.getImage(id)
            if (response.isSuccessful) {
                val imageDto = response.body()
                if (imageDto != null) {
                    val image = convertImageDtoToDomain(imageDto, serverAddressProvider.getBaseUrl())
                    // Cache to local database - inline conversion
                    imageDao.insertImage(ImageEntity(
                        id = image.id,
                        title = image.title,
                        url = image.url,
                        thumbnailUrl = image.thumbnailUrl,
                        width = image.width,
                        height = image.height,
                        fileSize = image.fileSize,
                        createdAt = image.createdAt,
                        isFavorite = image.isFavorite
                    ))
                    Result.Success(image)
                } else {
                    Result.Error(Exception("Empty response"), "Empty response")
                }
            } else {
                // Fallback to cache
                val cached = imageDao.getImageById(id)
                if (cached != null) {
                    Result.Success(cached.toDomainModel())
                } else {
                    Result.Error(HttpException(response), "Image not found")
                }
            }
        } catch (e: Exception) {
            // Fallback to cache on error
            try {
                val cached = imageDao.getImageById(id)
                if (cached != null) {
                    Result.Success(cached.toDomainModel())
                } else {
                    Result.Error(e, e.message ?: "Unknown error")
                }
            } catch (cacheError: Exception) {
                Result.Error(e, e.message ?: "Unknown error")
            }
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

    private fun convertImageDtoToDomain(dto: ImageDto, baseUrl: String): Image {
        // Handle null url
        val url = dto.url ?: ""
        val fullUrl = if (url.startsWith("http")) url else "$baseUrl$url"
        val fullThumbnailUrl = dto.thumbnailUrl?.let { 
            if (it.startsWith("http")) it else "$baseUrl$it" 
        }
        
        android.util.Log.d("ImageRepository", "convertImageDtoToDomain: id=${dto.id}")
        android.util.Log.d("ImageRepository", "  baseUrl=$baseUrl")
        android.util.Log.d("ImageRepository", "  url=$url")
        android.util.Log.d("ImageRepository", "  fullUrl=$fullUrl")
        android.util.Log.d("ImageRepository", "  thumbnailUrl=${dto.thumbnailUrl}")
        android.util.Log.d("ImageRepository", "  fullThumbnailUrl=$fullThumbnailUrl")
        
        return Image(
            id = dto.getStringId(),
            title = dto.title ?: "",
            url = fullUrl,
            thumbnailUrl = fullThumbnailUrl,
            width = dto.width ?: 0,
            height = dto.height ?: 0,
            fileSize = dto.fileSize ?: 0,
            createdAt = parseDateToTimestamp(dto.createdAt),
            isFavorite = dto.isFavorite ?: false
        )
    }

    private fun parseDateToTimestamp(dateString: String?): Long {
        if (dateString == null) return 0
        return try {
            java.time.Instant.parse(dateString).toEpochMilli()
        } catch (e: Exception) {
            0
        }
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
