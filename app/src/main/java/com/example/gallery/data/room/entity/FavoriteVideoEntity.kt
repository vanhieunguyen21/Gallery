package com.example.gallery.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteVideo")
data class FavoriteVideoEntity(
    @PrimaryKey
    val id: Long,
    val bucketId: Long,
)
