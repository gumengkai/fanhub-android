package com.funhub.android.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * API 响应数据模型
 */

@JsonClass(generateAdapter = true)
data class Video(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "path") val path: String,
    @Json(name = "source_id") val sourceId: Int,
    @Json(name = "file_size") val fileSize: Long,
    @Json(name = "duration") val duration: Int, // 秒
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "thumbnail_path") val thumbnailPath: String?,
    @Json(name = "is_favorite") val isFavorite: Boolean,
    @Json(name = "description") val description: String?,
    @Json(name = "view_count") val viewCount: Int,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "updated_at") val updatedAt: String,
    @Json(name = "tags") val tags: List<Tag> = emptyList(),
    @Json(name = "stream_url") val streamUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "path") val path: String,
    @Json(name = "source_id") val sourceId: Int,
    @Json(name = "file_size") val fileSize: Long,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "thumbnail_path") val thumbnailPath: String?,
    @Json(name = "is_favorite") val isFavorite: Boolean,
    @Json(name = "view_count") val viewCount: Int,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "file_url") val fileUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class Tag(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "color") val color: String = "#fb7299"
)

@JsonClass(generateAdapter = true)
data class Source(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String, // "local" or "nas"
    @Json(name = "path") val path: String,
    @Json(name = "is_active") val isActive: Boolean,
    @Json(name = "last_scan_at") val lastScanAt: String?
)

@JsonClass(generateAdapter = true)
data class WatchHistory(
    @Json(name = "id") val id: Int,
    @Json(name = "video_id") val videoId: Int,
    @Json(name = "playback_position") val playbackPosition: Int, // 秒
    @Json(name = "is_completed") val isCompleted: Boolean,
    @Json(name = "watched_at") val watchedAt: String,
    @Json(name = "video") val video: Video? = null
)

@JsonClass(generateAdapter = true)
data class VideoListResponse(
    @Json(name = "items") val items: List<Video>,
    @Json(name = "total") val total: Int,
    @Json(name = "page") val page: Int,
    @Json(name = "page_size") val pageSize: Int,
    @Json(name = "has_more") val hasMore: Boolean
)

@JsonClass(generateAdapter = true)
data class ImageListResponse(
    @Json(name = "items") val items: List<Image>,
    @Json(name = "total") val total: Int,
    @Json(name = "page") val page: Int,
    @Json(name = "page_size") val pageSize: Int,
    @Json(name = "has_more") val hasMore: Boolean
)

@JsonClass(generateAdapter = true)
data class FavoritesResponse(
    @Json(name = "videos") val videos: List<Video>,
    @Json(name = "images") val images: List<Image>,
    @Json(name = "total") val total: Int
)

@JsonClass(generateAdapter = true)
data class HistoryStats(
    @Json(name = "total_watched") val totalWatched: Int,
    @Json(name = "total_duration") val totalDuration: Int, // 秒
    @Json(name = "completed_count") val completedCount: Int
)
