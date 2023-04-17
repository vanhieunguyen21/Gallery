package com.example.gallery.ui.screen

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery.app.BaseApplication
import com.example.gallery.data.datastore.getSortOrder
import com.example.gallery.data.datastore.getSortType
import com.example.gallery.data.datastore.saveSortOrder
import com.example.gallery.data.datastore.saveSortType
import com.example.gallery.data.repository.MediaRepository
import com.example.gallery.data.datastore.toggleDarkTheme
import com.example.gallery.data.repository.FavoriteMediaRepository
import com.example.gallery.model.Bucket
import com.example.gallery.model.GalleryItem
import com.example.gallery.model.Image
import com.example.gallery.model.SortOrder
import com.example.gallery.model.SortType
import com.example.gallery.model.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val app: BaseApplication,
) : ViewModel() {

    var loading by mutableStateOf(false)
        private set

    var buckets: List<Bucket> by mutableStateOf(listOf())
        private set

    init {
        // Load all images and videos then create buckets for folders
        // Then put respective media item into those buckets
        viewModelScope.launch {
            // Set indicator to loading
            loading = true

            // Get all buckets
            buckets = mediaRepository.getAllBuckets()

            // Set indicator to finished loading
            loading = false
        }
    }

    fun getBucketThumbnail(bucket: Bucket, width: Int, height: Int) : Bitmap {
        return mediaRepository.getBucketThumbnail(bucket, width, height)
    }

    fun toggleDarkMode() {
        viewModelScope.launch { toggleDarkTheme(app) }
    }
}