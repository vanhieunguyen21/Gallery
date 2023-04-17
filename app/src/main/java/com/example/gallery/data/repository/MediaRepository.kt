package com.example.gallery.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.example.gallery.app.BaseApplication
import com.example.gallery.model.Bucket
import com.example.gallery.model.GalleryItem
import com.example.gallery.model.Image
import com.example.gallery.model.Video
import com.example.gallery.util.getBrokenThumbnail
import com.example.gallery.util.getMediaThumbnail
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val app: BaseApplication,
) {
    private val imageProjection = listOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.WIDTH,
        MediaStore.Images.Media.HEIGHT,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.VOLUME_NAME,
    )

    private val videoProjection = listOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DATE_TAKEN,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.WIDTH,
        MediaStore.Video.Media.HEIGHT,
        MediaStore.Video.Media.BUCKET_ID,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.VOLUME_NAME,
        MediaStore.Video.Media.DURATION,
    )

    private fun cursorToImages(cursor: Cursor): List<Image> {
        val result = mutableListOf<Image>()

        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
        val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
        val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
        val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
        val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val volumeNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.VOLUME_NAME)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val date = cursor.getLong(dateColumn)
            val size = cursor.getLong(sizeColumn)
            val width = cursor.getLong(widthColumn)
            val height = cursor.getLong(heightColumn)
            val bucketId = cursor.getLong(bucketIdColumn)
            var bucketName = cursor.getString(bucketNameColumn)
            val volumeName = cursor.getString(volumeNameColumn)

            val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            if (bucketName == null) bucketName = volumeName ?: "Unknown"

            result += Image(id, contentUri, name, date, width, height, size, bucketId, bucketName, volumeName)
        }

        return result
    }

    private fun cursorToVideos(cursor: Cursor): List<Video> {
        val result = mutableListOf<Video>()

        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
        val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)
        val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
        val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
        val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
        val volumeNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.VOLUME_NAME)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val date = cursor.getLong(dateColumn)
            val size = cursor.getLong(sizeColumn)
            val width = cursor.getLong(widthColumn)
            val height = cursor.getLong(heightColumn)
            val bucketId = cursor.getLong(bucketIdColumn)
            var bucketName = cursor.getString(bucketNameColumn)
            val volumeName = cursor.getString(volumeNameColumn)
            val duration = cursor.getLong(durationColumn)
            val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

            if (bucketName == null) bucketName = volumeName ?: "Unknown"

            result += Video(id, contentUri, name, date, width, height, size, bucketId, bucketName, volumeName, duration)
        }

        return result
    }

    fun getAllBuckets(): List<Bucket> {
        val belowQ = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        val buckets = mutableSetOf<Bucket>()
        val imageCollection = if (belowQ) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val videoCollection = if (belowQ) MediaStore.Video.Media.EXTERNAL_CONTENT_URI else
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

        val imageBucketProjection = listOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.VOLUME_NAME,
        )
        val videoBucketProjection = listOf(
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.VOLUME_NAME,
        )
        val selection = null
        val selectionArgs = null
        val sortOrder = null
        // Query for image buckets
        val imageQuery = app.contentResolver.query(
            imageCollection, imageBucketProjection.toTypedArray(), selection, selectionArgs, sortOrder,
        )
        imageQuery?.use { cursor ->
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val volumeNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.VOLUME_NAME)

            while (cursor.moveToNext()) {
                val bucketId = cursor.getLong(bucketIdColumn)
                var bucketName = cursor.getString(bucketNameColumn)
                val volumeName = cursor.getString(volumeNameColumn)
                if (bucketName == null) bucketName = volumeName ?: "Unknown"

                buckets.add(Bucket(bucketId, bucketName))
            }
        }
        // Query for video buckets
        val videoQuery = app.contentResolver.query(
            videoCollection, videoBucketProjection.toTypedArray(), selection, selectionArgs, sortOrder,
        )
        videoQuery?.use { cursor ->
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
            val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val volumeNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.VOLUME_NAME)

            while (cursor.moveToNext()) {
                val bucketId = cursor.getLong(bucketIdColumn)
                var bucketName = cursor.getString(bucketNameColumn)
                val volumeName = cursor.getString(volumeNameColumn)
                if (bucketName == null) bucketName = volumeName ?: "Unknown"

                buckets.add(Bucket(bucketId, bucketName))
            }
        }

        return buckets.toList().sortedBy { it.name }
    }

    fun getBucketImages(bucket: Bucket): List<Image> {
        val belowQ = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        val images = mutableListOf<Image>()
        val collection = if (belowQ) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(bucket.id.toString())
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val query = app.contentResolver.query(
            collection, imageProjection.toTypedArray(), selection, selectionArgs, sortOrder
        )
        query?.use { cursor ->
            images += cursorToImages(cursor)
        }
        return images.toList()
    }

    fun getBucketVideos(bucket: Bucket): List<Video> {
        val belowQ = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        val videos = mutableListOf<Video>()
        val collection = if (belowQ) MediaStore.Video.Media.EXTERNAL_CONTENT_URI else
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

        val selection = "${MediaStore.Video.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(bucket.id.toString())
        val sortOrder = "${MediaStore.Video.Media.DATE_TAKEN} DESC"
        val query = app.contentResolver.query(
            collection, videoProjection.toTypedArray(), selection, selectionArgs, sortOrder
        )
        query?.use { cursor ->
            videos += cursorToVideos(cursor)
        }
        return videos.toList()
    }

    fun getBucketMedia(bucket: Bucket): List<GalleryItem> {
        val images = getBucketImages(bucket)
        val videos = getBucketVideos(bucket)
        // Merge images and videos list, preserve sorted order
        val result = mutableListOf<GalleryItem>()
        var iIdx = 0
        var vIdx = 0
        while (iIdx < images.size && vIdx < videos.size) {
            if (images[iIdx].addedTimestamp >= videos[vIdx].addedTimestamp) result += images[iIdx++]
            else result += videos[vIdx++]
        }
        while (iIdx < images.size) result += images[iIdx++]
        while (vIdx < videos.size) result += videos[vIdx++]
        return result
    }

    fun getBucketThumbnail(bucket: Bucket, width: Int, height: Int): Bitmap {
        // Get the latest image and video from bucket
        val belowQ = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        var image: Image? = null
        var video: Video? = null

        val imageCollection = if (belowQ) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val videoCollection = if (belowQ) MediaStore.Video.Media.EXTERNAL_CONTENT_URI else
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val imageSelection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val videoSelection = "${MediaStore.Video.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(bucket.id.toString())
        val sortOrder = "LIMIT 1"

        // Query for the latest image in the bucket
        val imageQuery = if (Build.VERSION.SDK_INT >= 26) {
            app.contentResolver.query(
                imageCollection,
                imageProjection.toTypedArray(),
                Bundle().apply {
                    putInt(ContentResolver.QUERY_ARG_LIMIT, 1)
                    putString(ContentResolver.QUERY_ARG_SQL_SELECTION, imageSelection)
                    putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)
                },
                null)
        } else {
            app.contentResolver.query(
                imageCollection,
                imageProjection.toTypedArray(),
                imageSelection,
                selectionArgs,
                sortOrder,
            )
        }
        imageQuery?.use {
            val images = cursorToImages(it)
            image = if (images.isEmpty()) null else images[0]
        }

        // Query for the latest video in the bucket
        val videoQuery = if (Build.VERSION.SDK_INT >= 26) {
            app.contentResolver.query(
                videoCollection,
                videoProjection.toTypedArray(),
                Bundle().apply {
                    putInt(ContentResolver.QUERY_ARG_LIMIT, 1)
                    putString(ContentResolver.QUERY_ARG_SQL_SELECTION, videoSelection)
                    putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)
                },
                null)
        } else {
            app.contentResolver.query(
                videoCollection,
                videoProjection.toTypedArray(),
                videoSelection,
                selectionArgs,
                sortOrder,
            )
        }

        videoQuery?.use {
            val videos = cursorToVideos(it)
            video = if (videos.isEmpty()) null else videos[0]
        }
        val latestMedia: GalleryItem? =
            if (image == null && video == null) null
            else if (image == null) video
            else if (video == null) image
            else if (image!!.addedTimestamp >= video!!.addedTimestamp) image
            else video

        return if (latestMedia != null) getMediaThumbnail(app, latestMedia, width, height)
        else getBrokenThumbnail(app, width, height)
    }

    fun getAllVideos(): List<Video> {
        val belowQ = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        val videos = mutableListOf<Video>()
        val collection = if (belowQ) MediaStore.Video.Media.EXTERNAL_CONTENT_URI else
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

        val selection = null
        val selectionArgs = null
        val sortOrder = null
        val query = app.contentResolver.query(
            collection, videoProjection.toTypedArray(), selection, selectionArgs, sortOrder
        )
        query?.use { cursor ->
            videos += cursorToVideos(cursor)
        }
        return videos.toList()
    }
}