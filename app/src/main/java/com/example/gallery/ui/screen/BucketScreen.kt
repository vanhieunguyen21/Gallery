package com.example.gallery.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gallery.R
import com.example.gallery.model.Image
import com.example.gallery.model.Video
import com.example.gallery.ui.component.ImageItem
import com.example.gallery.ui.component.SimpleTopAppBar
import com.example.gallery.ui.component.VideoItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BucketScreen(
    navController: NavController,
    viewModel: BucketViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        SimpleTopAppBar(
            title = viewModel.bucket.name,
            navigationButton = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                    )
                }
            },
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 20.dp, horizontal = 4.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(viewModel.items, key = { "${it.id}_${it.favorite}" }) {
                val modifier = remember {
                    Modifier.combinedClickable(onClick = {}, onLongClick = {
                        if (it.favorite) viewModel.unFavoriteItem(it)
                        else viewModel.favoriteItem(it)
                    })
                }
                if (it is Image) ImageItem(it, modifier = modifier)
                else if (it is Video) VideoItem(it, modifier = modifier)
            }
        }
    }
}