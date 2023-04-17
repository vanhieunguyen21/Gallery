package com.example.gallery.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery.data.repository.FavoriteMediaRepository
import com.example.gallery.data.repository.MediaRepository
import com.example.gallery.model.Bucket
import com.example.gallery.model.GalleryItem
import com.example.gallery.model.Image
import com.example.gallery.model.Video
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BucketViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository,
    private val favoriteMediaRepository: FavoriteMediaRepository,
) : ViewModel() {
    val bucket: Bucket

    var items: List<GalleryItem> by mutableStateOf(listOf())
        private set

    init {
        // Get bucket data
        val bucketJson: String = savedStateHandle["bucket"]!!
        bucket = Gson().fromJson(bucketJson, Bucket::class.java)

        viewModelScope.launch {
            // Get all items from bucket
            val media = mediaRepository.getBucketMedia(bucket)
            // Load favorite
            val favoriteIds = favoriteMediaRepository.getBucketFavoriteMediaIds(bucket.id)
            media.forEach { item ->
                if (favoriteIds.contains(item.id)) item.favorite = true
            }
            // Sort the items
            items = media.sortedWith(compareByDescending<GalleryItem> { it.favorite }.thenByDescending { it.addedTimestamp })
        }
    }

    fun favoriteItem(galleryItem: GalleryItem) {
        viewModelScope.launch {
            // Add favorite to database
            if (galleryItem is Image) favoriteMediaRepository.addFavoriteImage(galleryItem.id, bucket.id)
            else if (galleryItem is Video) favoriteMediaRepository.addFavoriteVideo(galleryItem.id, bucket.id)

            // Search for item in current bucket
            val newItems = items.toMutableList()
            val index = newItems.indexOfFirst { it.id == galleryItem.id }
            if (index >= 0) {
                // Set item to favorite and change its position
                val item = newItems[index]
                item.favorite = true
                newItems.removeAt(index)
                // Find the position to put the item
                val newIndex = newItems.indexOfFirst { !it.favorite || it.addedTimestamp < item.addedTimestamp }
                if (newIndex == -1) newItems.add(item)
                else newItems.add(newIndex, item)
            }
            // Reassign the value to notify change
            items = newItems
        }
    }

    fun unFavoriteItem(galleryItem: GalleryItem) {
        viewModelScope.launch {
            // Remove favorite from database
            if (galleryItem is Image) favoriteMediaRepository.removeFavoriteImage(galleryItem.id)
            else if (galleryItem is Video) favoriteMediaRepository.removeFavoriteImage(galleryItem.id)
            // Search for item in current bucket
            val newItems = items.toMutableList()
            val index = newItems.indexOfFirst { it.id == galleryItem.id }
            if (index >= 0) {
                // Remove favorite from item and change its position
                val item = newItems[index]
                item.favorite = false
                newItems.removeAt(index)
                // Find the position to put the item
                val newIndex = newItems.indexOfFirst { !it.favorite && it.addedTimestamp < item.addedTimestamp }
                if (newIndex == -1) newItems.add(item)
                else newItems.add(newIndex, item)
            }
            // Reassign the value to notify change
            items = newItems
        }
    }
}