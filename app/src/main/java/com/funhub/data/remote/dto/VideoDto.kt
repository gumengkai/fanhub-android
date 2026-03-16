package com.funhub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideoDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("thumbnail_url") val thumbnailUrl: String?,
    @SerializedName("duration") val duration: Long,
    @SerializedName("file_size") val fileSize: Long,
    @SerializedName("created_at") val createdAt: Long
)

data class VideoListResponse(
    @SerializedName("videos") val videos: List<VideoDto>,
    @SerializedName("total") val total: Int
)

data class VideoClipInfoDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("duration") val duration: Long,
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
