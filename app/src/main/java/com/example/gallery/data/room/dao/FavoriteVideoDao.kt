package com.example.gallery.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.gallery.data.room.entity.FavoriteVideoEntity

@Dao
interface FavoriteVideoDao {
    @Query("INSERT OR REPLACE INTO favoriteVideo(id, bucketId) VALUES(:id, :bucketId)")
    suspend fun add(id: Long, bucketId: Long)

    @Query("DELETE FROM favoriteVideo WHERE id=:id")
    suspend fun remove(id: Long)

    @Query("SELECT * FROM favoriteVideo WHERE bucketId=:bucketId")
    suspend fun getBucketFavorite(bucketId: Long): List<FavoriteVideoEntity>
}