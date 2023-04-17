package com.example.gallery.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gallery.data.room.dao.FavoriteImageDao
import com.example.gallery.data.room.dao.FavoriteVideoDao
import com.example.gallery.data.room.entity.FavoriteImageEntity
import com.example.gallery.data.room.entity.FavoriteVideoEntity

@Database(entities = [FavoriteImageEntity::class, FavoriteVideoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteImageDao() : FavoriteImageDao
    abstract fun favoriteVideoDao() : FavoriteVideoDao
}