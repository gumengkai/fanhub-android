package com.funhub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Video DTO matching backend API response
 * Backend uses Int for id, but we convert to String for consistency
 */
data class VideoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("thumbnail_url") val thumbnailUrl: String?,
    @SerializedName("duration") val duration: Int,
    @SerializedName("file_size") val fileSize: Long,
    @SerializedName("created_at") val createdAt: String?, // ISO format from backend
    @SerializedName("is_favorite") val isFavorite: Boolean = false,
    @SerializedName("view_count") val viewCount: Int = 0,
    @SerializedName("tags") val tags: List<TagDto>? = null
) {
    fun getStringId(): String = id.toString()
}

/**
 * Video list response from backend
 * Backend returns {items: [...], total: ...} not {videos: [...]}
 */
data class VideoListResponse(
    @SerializedName("items") val items: List<VideoDto> = emptyList(),
    @SerializedName("total") val total: Int = 0,
    @SerializedName("pages") val pages: Int = 0,
    @SerializedName("current_page") val currentPage: Int = 1,
    @SerializedName("per_page") val perPage: Int = 20
)

data class VideoClipInfoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("thumbnail_url") val thumbnailUrl: String?
)

data class ClipRequest(
    @SerializedName("start_time") val startTime: Long,
    @SerializedName("end_time") val endTime: Long
)

data class ClipResponse(
    @SerializedName("task_id") val taskId: String,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?
)

data class HealthResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?
)

data class FFmpegCheckResponse(
    @SerializedName("available") val available: Boolean,
    @SerializedName("version") val version: String?
)
