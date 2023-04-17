package com.example.gallery.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.gallery.data.room.entity.FavoriteImageEntity

@Dao
interface FavoriteImageDao {
    @Query("INSERT OR REPLACE INTO favoriteImage(id, bucketId) VALUES(:id, :bucketId)")
    suspend fun add(id: Long, bucketId: Long)

    @Query("DELETE FROM favoriteImage WHERE id=:id")
    suspend fun remove(id: Long)

    @Query("SELECT * FROM favoriteImage WHERE bucketId=:bucketId")
    suspend fun getBucketFavorite(bucketId: Long): List<FavoriteImageEntity>
}