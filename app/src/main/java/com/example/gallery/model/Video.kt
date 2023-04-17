package com.example.gallery.model

import android.net.Uri

class Video(
    id: Long,
    uri: Uri,
    name: String,
    addedTimestamp: Long,
    width: Long,
    height: Long,
    size: Long,
    bucketId: Long,
    bucketName: String,
    volumeName: String,
    val duration: Long,
) : GalleryItem(id, uri, name, addedTimestamp, width, height, size, bucketId, bucketName, volumeName)