package com.example.gallery.data.repository

import com.example.gallery.data.room.AppDatabase
import javax.inject.Inject

class FavoriteMediaRepository @Inject constructor(
    private val appDatabase: AppDatabase,
) {
    suspend fun addFavoriteImage(id: Long, bucketId: Long) {
        appDatabase.favoriteImageDao().add(id, bucketId)
    }

    suspend fun addFavoriteVideo(id: Long, bucketId: Long) {
        appDatabase.favoriteVideoDao().add(id, bucketId)
    }

    suspend fun removeFavoriteImage(id: Long) {
        appDatabase.favoriteImageDao().remove(id)
    }

    suspend fun removeFavoriteVideo(id: Long) {
        appDatabase.favoriteVideoDao().remove(id)
    }

    suspend fun getBucketFavoriteImageIds(bucketId: Long): Set<Long> {
        return appDatabase.favoriteImageDao().getBucketFavorite(bucketId).map { it.id }.toSet()
    }

    suspend fun getBucketFavoriteVideoIds(bucketId: Long): Set<Long> {
        return appDatabase.favoriteVideoDao().getBucketFavorite(bucketId).map { it.id }.toSet()
    }

    suspend fun getBucketFavoriteMediaIds(bucketId: Long): Set<Long> {
        val imageIds = appDatabase.favoriteImageDao().getBucketFavorite(bucketId).map { it.id }
        val videoIds = appDatabase.favoriteVideoDao().getBucketFavorite(bucketId).map { it.id }
        return (imageIds + videoIds).toSet()
    }
}