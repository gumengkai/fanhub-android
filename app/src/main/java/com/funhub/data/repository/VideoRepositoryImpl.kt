package com.funhub.data.repository

import android.util.Log
import com.funhub.data.local.dao.VideoDao
import com.funhub.data.local.entity.VideoEntity
import com.funhub.data.remote.api.FunHubApi
import com.funhub.data.remote.dto.VideoDto
import com.funhub.domain.model.Result
import com.funhub.domain.model.Video
import com.funhub.domain.model.VideoClipInfo
import com.funhub.domain.repository.VideoRepository
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

    companion object {
        private const val TAG = "VideoRepository"
    }

    override suspend fun getVideos(): Result<List<Video>> {
        return try {
            Log.d(TAG, "Getting videos from API...")
            val baseUrl = serverAddressProvider.getBaseUrl()
            Log.d(TAG, "Base URL: $baseUrl")
            
            val response = api.getVideos()
            Log.d(TAG, "Response code: ${response.code()}")
            
            if (response.isSuccessful) {
                val body = response.body()
                Log.d(TAG, "Response body null: ${body == null}")
                
                if (body == null) {
                    Log.e(TAG, "Response body is null")
                    return Result.Error(Exception("Empty response"), "Empty response")
                }
                
                Log.d(TAG, "Items count: ${body.items.size}")
                Log.d(TAG, "Total from API: ${body.total}")
                
                val videos = body.items.mapIndexed { index, dto ->
                    Log.d(TAG, "Converting video $index: id=${dto.id}, title=${dto.title.take(20)}")
                    dto.toDomainModel(baseUrl)
                }
                
                Log.d(TAG, "Converted ${videos.size} videos successfully")
                Result.Success(videos)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "API error: ${response.code()}, body: $errorBody")
                Result.Error(HttpException(response), "Failed to load videos: ${response.code()}")
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error: ${e.message}", e)
            Result.Error(e, "Network error: ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error: ${e.message}", e)
            Result.Error(e, e.message ?: "Unknown error")
        }
    }

    override suspend fun getVideoById(id: String): Result<Video> {
        return try {
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
                            id = info.id.toString(),
                            title = info.title,
                            duration = info.duration.toLong(),
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
            val response = api.createClip(videoId, com.funhub.data.remote.dto.ClipRequest(startTime, endTime))
            if (response.isSuccessful) {
                val taskId = response.body()?.taskId
                if (taskId != null) {
                    Result.Success(taskId)
                } else {
                    Result.Error(Exception("No task ID"), "No task ID")
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
            Result.Success(response.body()?.available ?: false)
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Unknown error")
        }
    }

    private fun VideoDto.toDomainModel(baseUrl: String): Video {
        return Video(
            id = getStringId(),
            title = title,
            description = description,
            thumbnailUrl = thumbnailUrl?.let { if (it.startsWith("http")) it else "$baseUrl$it" },
            streamUrl = "$baseUrl/api/videos/$id/stream",
            duration = duration.toLong(),
            fileSize = fileSize,
            createdAt = parseDateToTimestamp(createdAt),
            isFavorite = isFavorite,
            viewCount = viewCount,
            creatorName = ""
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
            thumbnailUrl = thumbnailUrl?.let { if (it.startsWith("http")) it else "$baseUrl$it" },
            streamUrl = streamUrl,
            duration = duration,
            fileSize = fileSize,
            createdAt = createdAt,
            isFavorite = isFavorite,
            viewCount = 0,
            creatorName = ""
        )
    }
}
