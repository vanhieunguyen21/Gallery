package com.example.gallery.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.core.graphics.drawable.toBitmap
import com.example.gallery.R
import com.example.gallery.model.GalleryItem
import com.example.gallery.model.Image
import com.example.gallery.model.Video

fun getImageThumbnail(context: Context, image: Image, width: Int, height: Int): Bitmap {
    try {
        val bmp = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val thumbnailKind = if (width > 96 || height > 96) MediaStore.Images.Thumbnails.MINI_KIND
            else MediaStore.Images.Thumbnails.MICRO_KIND
            MediaStore.Images.Thumbnails.getThumbnail(context.contentResolver, image.id, thumbnailKind, null)
        } else {
            context.contentResolver.loadThumbnail(image.uri, Size(width, height), null)
        }
        return bmp ?: getBrokenThumbnail(context, width, height)
    } catch (ex: Exception) {
        ex.printStackTrace()
        return getBrokenThumbnail(context, width, height)
    }
}

fun getVideoThumbnail(context: Context, video: Video, width: Int, height: Int): Bitmap {
    try {
        val bmp = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val thumbnailKind = if (width > 96 || height > 96) MediaStore.Video.Thumbnails.MINI_KIND
            else MediaStore.Video.Thumbnails.MICRO_KIND
            MediaStore.Video.Thumbnails.getThumbnail(context.contentResolver, video.id, thumbnailKind, null)
        } else {
            context.contentResolver.loadThumbnail(video.uri, Size(width, height), null)
        }
        return bmp ?: getBrokenThumbnail(context, width, height)
    } catch (ex: Exception) {
        ex.printStackTrace()
        return getBrokenThumbnail(context, width, height)
    }
}

fun getMediaThumbnail(context: Context, media: GalleryItem, width: Int, height: Int): Bitmap {
    return when (media) {
        is Image -> getImageThumbnail(context, media, width, height)
        is Video -> getVideoThumbnail(context, media, width, height)
        else -> getBrokenThumbnail(context, width, height)
    }
}

fun getBrokenThumbnail(context: Context, width: Int, height: Int): Bitmap {
    return context.resources.getDrawable(R.drawable.ic_broken_image, null).toBitmap(width, height)
}