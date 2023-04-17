package com.example.gallery.ui.screen

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gallery.R
import com.example.gallery.ui.component.BucketItem
import com.example.gallery.ui.component.SimpleTopAppBar
import com.example.gallery.ui.component.SortBar
import com.example.gallery.ui.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        SimpleTopAppBar(
            title = stringResource(R.string.app_name),
            menuButton = {
                IconButton(onClick = { viewModel.toggleDarkMode() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_dark_mode),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            items(viewModel.buckets, key = { it.id }) {
                val thumbnail = remember { viewModel.getBucketThumbnail(it, 300, 300) }

                BucketItem(
                    bucket = it,
                    thumbnail = thumbnail,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.BucketScreen.routeWithArgs(it))
                    }
                )
            }
        }
    }
}