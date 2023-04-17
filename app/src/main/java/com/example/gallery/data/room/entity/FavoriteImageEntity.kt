package com.example.gallery.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteImage")
data class FavoriteImageEntity(
    @PrimaryKey
    val id: Long,
    val bucketId: Long,
)
