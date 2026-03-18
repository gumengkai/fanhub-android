package com.funhub.data.repository

import com.funhub.data.local.dao.VideoDao
import com.funhub.data.local.entity.VideoEntity
import com.funhub.data.remote.api.FunHubApi
import com.funhub.data.remote.dto.VideoDto
import com.funhub.domain.model.Result
import com.funhub.domain.model.Video
import com.funhub.domain.model.VideoClipInfo
import com.funhub.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepositoryImpl @Inject constructor(
    private val api: FunHubApi,
    private val videoDao: VideoDao,
    private val serverAddressProvider: ServerAddressProvider
) : VideoRepository {

    override suspend fun getVideos(): Result<List<Video>> {
        return try {
            val response = api.getVideos()
            if (response.isSuccessful) {
                val videos = response.body()?.videos?.map { it.toDomainModel(serverAddressProvider.getBaseUrl()) } ?: emptyList()
                // Cache to local database
                videoDao.insertVideos(videos.map { it.toEntity() })
                Result.Success(videos)
            } else {
                Result.Error(HttpException(response), "Failed to load videos")
            }
        } catch (e: IOException) {
            // Return cached data on network error
            val cachedVideos = videoDao.getAllVideos()
            Result.Success(cachedVideos.map { it.toDomainModel(serverAddressProvider.getBaseUrl()) })
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Unknown error")
        }
    }

    override suspend fun getVideoById(id: String): Result<Video> {
        return try {
            // Try to get from cache first
            val cached = videoDao.getVideoById(id)
            if (cached != null) {
                Result.Success(cached.toDomainModel(serverAddressProvider.getBaseUrl()))
            } else {
                Result.Error(Exception("Video not found"), "Video not found")
            }
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Unknown error")
        }
    }

    override suspend fun getFavoriteVideos(): List<Video> {
        return videoDao.getFavoriteVideos().map { 
            it.toDomainModel(serverAddressProvider.getBaseUrl()) 
        }
    }

    override suspend fun toggleFavorite(videoId: String, isFavorite: Boolean) {
        videoDao.updateFavoriteStatus(videoId, isFavorite)
    }

    override suspend fun getClipInfo(videoId: String): Result<VideoClipInfo> {
        return try {
            val response = api.getClipInfo(videoId)
            if (response.isSuccessful) {
                val info = response.body()
                if (info != null) {
                    Result.Success(
                        VideoClipInfo(
                            id = info.id,
                            title = info.title,
                            duration = info.duration,
                            thumbnailUrl = info.thumbnailUrl?.let { serverAddressProvider.getBaseUrl() + it }
                        )
                    )
                } else {
                    Result.Error(Exception("Empty response"), "Empty response")
                }
            } else {
                Result.Error(HttpException(response), "Failed to get clip info")
            }
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Unknown error")
        }
    }

    override suspend fun createClip(videoId: String, startTime: Long, endTime: Long): Result<String> {
        return try {
            val response = api.createClip(
                videoId,
                com.funhub.data.remote.dto.ClipRequest(startTime, endTime)
            )
            if (response.isSuccessful) {
                val taskId = response.body()?.taskId
                if (taskId != null) {
                    Result.Success(taskId)
                } else {
                    Result.Error(Exception("No task ID returned"), "No task ID returned")
                }
            } else {
                Result.Error(HttpException(response), "Failed to create clip")
            }
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Unknown error")
        }
    }

    override suspend fun checkFFmpeg(): Result<Boolean> {
        return try {
            val response = api.checkFFmpeg()
            if (response.isSuccessful) {
                Result.Success(response.body()?.available ?: false)
            } else {
                Result.Success(false)
            }
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Unknown error")
        }
    }

    private fun VideoDto.toDomainModel(baseUrl: String): Video {
        return Video(
            id = id,
            title = title,
            description = description,
            thumbnailUrl = thumbnailUrl?.let { baseUrl + it },
            streamUrl = "$baseUrl/api/videos/$id/stream",
            duration = duration,
            fileSize = fileSize,
            createdAt = createdAt,
            isFavorite = false
        )
    }

    private fun Video.toEntity(): VideoEntity {
        return VideoEntity(
            id = id,
            title = title,
            description = description,
            thumbnailUrl = thumbnailUrl,
            streamUrl = streamUrl,
            duration = duration,
            fileSize = fileSize,
            createdAt = createdAt,
            isFavorite = isFavorite
        )
    }

    private fun VideoEntity.toDomainModel(baseUrl: String): Video {
        return Video(
            id = id,
            title = title,
            description = description,
            thumbnailUrl = thumbnailUrl?.let { baseUrl + it },
            streamUrl = streamUrl,
            duration = duration,
            fileSize = fileSize,
            createdAt = createdAt,
            isFavorite = isFavorite
        )
    }
}


