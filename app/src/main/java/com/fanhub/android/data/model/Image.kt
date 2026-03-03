package com.fanhub.android.data.model

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("file_path") val filePath: String,
    @SerializedName("file_size") val fileSize: Long,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("thumbnail_path") val thumbnailPath: String?,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("source_id") val sourceId: Int?
)

data class ImageListResponse(
    @SerializedName("items") val items: List<Image>,
    @SerializedName("total") val total: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("per_page") val perPage: Int
)
