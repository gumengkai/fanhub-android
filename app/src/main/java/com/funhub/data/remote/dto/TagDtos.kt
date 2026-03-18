package com.funhub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TagDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val color: String
)

data class TagListResponse(
    @SerializedName("tags")
    val tags: List<TagDto>
)

data class CreateTagRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val color: String = "#1890ff"
)

data class UpdateTagRequest(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("color")
    val color: String? = null
)

data class UpdateVideoRequest(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("is_favorite")
    val isFavorite: Boolean? = null,
    @SerializedName("tag_ids")
    val tagIds: List<Int>? = null
)

data class UpdateImageRequest(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("is_favorite")
    val isFavorite: Boolean? = null,
    @SerializedName("tag_ids")
    val tagIds: List<Int>? = null
)

data class VideoFilterParams(
    val page: Int = 1,
    val perPage: Int = 20,
    val search: String? = null,
    val tagId: Int? = null,
    val favorite: Boolean? = null,
    val sortBy: String = "created_at",
    val order: String = "desc"
)

data class ImageFilterParams(
    val page: Int = 1,
    val perPage: Int = 20,
    val search: String? = null,
    val tagId: Int? = null,
    val favorite: Boolean? = null
)
