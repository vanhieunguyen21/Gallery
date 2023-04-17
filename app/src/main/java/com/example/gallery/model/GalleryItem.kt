package com.example.gallery.model

import android.net.Uri

open class GalleryItem(
    val id: Long,
    val uri: Uri,
    val name: String,
    val addedTimestamp: Long,
    val width: Long,
    val height: Long,
    val size: Long,
    val bucketId: Long,
    val bucketName: String,
    val volumeName: String,
    var favorite: Boolean = false,
)