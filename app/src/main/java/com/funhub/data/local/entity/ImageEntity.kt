package com.funhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val url: String,
    val thumbnailUrl: String?,
    val width: Int,
    val height: Int,
    val fileSize: Long,
    val createdAt: Long,
    val isFavorite: Boolean = false,
    val cachedAt: Long = System.currentTimeMillis()
)
