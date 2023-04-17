package com.example.gallery.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery.app.BaseApplication
import com.example.gallery.data.datastore.getSortOrder
import com.example.gallery.data.datastore.getSortType
import com.example.gallery.data.datastore.saveSortOrder
import com.example.gallery.data.datastore.saveSortType
import com.example.gallery.data.repository.FavoriteMediaRepository
import com.example.gallery.data.repository.MediaRepository
import com.example.gallery.model.Bucket
import com.example.gallery.model.GalleryItem
import com.example.gallery.model.Image
import com.example.gallery.model.SortOrder
import com.example.gallery.model.SortType
import com.example.gallery.model.Video
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BucketViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository,
    private val favoriteMediaRepository: FavoriteMediaRepository,
    private val app: BaseApplication,
) : ViewModel() {
    val bucket: Bucket

    private val sortTypeFlow: Flow<SortType> = getSortType(app)
    var sortType by mutableStateOf(SortType.Name)
        private set

    private val sortOrderFlow: Flow<SortOrder> = getSortOrder(app)
    var sortOrder by mutableStateOf(SortOrder.Ascending)
        private set

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
            // Set the items
            setMediaList(media)
        }

        // Collect sort settings
        viewModelScope.launch {
            sortTypeFlow.collectLatest {
                if (it != sortType) {
                    sortType = it
                    setMediaList(items)
                }
            }
        }
        viewModelScope.launch {
            sortOrderFlow.collectLatest {
                if (it != sortOrder) {
                    sortOrder = it
                    setMediaList(items)
                }
            }
        }
    }

    private fun setMediaList(mediaList: List<GalleryItem>) {
        this.items = when(sortType) {
            SortType.Name -> when(sortOrder) {
                SortOrder.Ascending -> mediaList.sortedWith(compareByDescending<GalleryItem> { it.favorite }.thenBy { it.name })
                SortOrder.Descending -> mediaList.sortedWith(compareByDescending<GalleryItem> { it.favorite }.thenByDescending { it.name })
            }
            SortType.LastModified -> when(sortOrder) {
                SortOrder.Ascending -> mediaList.sortedWith(compareByDescending<GalleryItem> { it.favorite }.thenBy { it.addedTimestamp })
                SortOrder.Descending -> mediaList.sortedWith(compareByDescending<GalleryItem> { it.favorite }.thenByDescending { it.addedTimestamp })
            }
            SortType.Size -> when(sortOrder) {
                SortOrder.Ascending -> mediaList.sortedWith(compareByDescending<GalleryItem> { it.favorite }.thenBy { it.size })
                SortOrder.Descending -> mediaList.sortedWith(compareByDescending<GalleryItem> { it.favorite }.thenByDescending { it.size })
            }
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

    fun onSortTypeChange(sortType: SortType, onChanged: () -> Unit) {
        viewModelScope.launch {
            saveSortType(app, sortType)
            onChanged()
        }
    }

    fun onSortOrderChange(sortOrder: SortOrder, onChanged: () -> Unit) {
        viewModelScope.launch {
            saveSortOrder(app, sortOrder)
            onChanged()
        }
    }
}