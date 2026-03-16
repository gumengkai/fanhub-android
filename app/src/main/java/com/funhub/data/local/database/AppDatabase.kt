package com.funhub.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.funhub.data.local.dao.ImageDao
import com.funhub.data.local.dao.VideoDao
import com.funhub.data.local.entity.ImageEntity
import com.funhub.data.local.entity.VideoEntity

@Database(
    entities = [VideoEntity::class, ImageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun imageDao(): ImageDao
}
