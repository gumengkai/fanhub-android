package com.funhub.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String?,
    val thumbnailUrl: String?,
    val streamUrl: String,
    val duration: Long,
    val fileSize: Long,
    val createdAt: Long,
    val isFavorite: Boolean = false,
    val cachedAt: Long = System.currentTimeMillis()
)
