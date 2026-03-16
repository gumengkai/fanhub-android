package com.funhub.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.funhub.data.local.entity.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    
    @Query("SELECT * FROM images ORDER BY createdAt DESC")
    fun getAllImages(): Flow<List<ImageEntity>>
    
    @Query("SELECT * FROM images WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteImages(): Flow<List<ImageEntity>>
    
    @Query("SELECT * FROM images WHERE id = :id")
    suspend fun getImageById(id: String): ImageEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: ImageEntity)
    
    @Update
    suspend fun updateImage(image: ImageEntity)
    
    @Query("UPDATE images SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)
    
    @Query("DELETE FROM images")
    suspend fun deleteAllImages()
}
