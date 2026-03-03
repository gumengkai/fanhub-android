package com.fanhub.android.data.model

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("file_path") val filePath: String,
    @SerializedName("file_size") val fileSize: Long,
    @SerializedName("duration") val duration: Long,
    @SerializedName("resolution") val resolution: String?,
    @SerializedName("thumbnail_path") val thumbnailPath: String?,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("view_count") val viewCount: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("tags") val tags: List<Tag> = emptyList(),
    @SerializedName("source_id") val sourceId: Int?,
    @SerializedName("watch_progress") val watchProgress: Float? = null,
    @SerializedName("is_watched") val isWatched: Boolean = false
)

data class VideoListResponse(
    @SerializedName("items") val items: List<Video>,
    @SerializedName("total") val total: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("per_page") val perPage: Int
)
