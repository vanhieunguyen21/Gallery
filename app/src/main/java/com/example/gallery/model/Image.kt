package com.example.gallery.model

import android.net.Uri

class Image(
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
) : GalleryItem(id, uri, name, addedTimestamp, width, height, size, bucketId, bucketName, volumeName)