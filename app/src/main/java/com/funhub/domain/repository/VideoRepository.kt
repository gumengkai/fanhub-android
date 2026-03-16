package com.funhub.domain.repository

import com.funhub.domain.model.Result
import com.funhub.domain.model.Video
import com.funhub.domain.model.VideoClipInfo
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    suspend fun getVideos(): Result<List<Video>>
    suspend fun getVideoById(id: String): Result<Video>
    fun getFavoriteVideos(): Flow<List<Video>>
    suspend fun toggleFavorite(videoId: String, isFavorite: Boolean)
    suspend fun getClipInfo(videoId: String): Result<VideoClipInfo>
    suspend fun createClip(videoId: String, startTime: Long, endTime: Long): Result<String>
    suspend fun checkFFmpeg(): Result<Boolean>
}
