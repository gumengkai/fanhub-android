package com.funhub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Image DTO matching backend API response
 * Backend uses Int for id
 */
data class ImageDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerializedName("width") val width: Int? = null,
    @SerializedName("height") val height: Int? = null,
    @SerializedName("file_size") val fileSize: Long? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("is_favorite") val isFavorite: Boolean? = false,
    @SerializedName("tags") val tags: List<TagDto>? = null
) {
    fun getStringId(): String = id?.toString() ?: ""
}

/**
 * Image list response from backend
 * Backend returns {items: [...], total: ...}
 */
data class ImageListResponse(
    @SerializedName("items") val items: List<ImageDto> = emptyList(),
    @SerializedName("total") val total: Int = 0,
    @SerializedName("pages") val pages: Int = 0,
    @SerializedName("current_page") val currentPage: Int = 1,
    @SerializedName("per_page") val perPage: Int = 20
)
