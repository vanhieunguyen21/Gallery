package com.example.gallery.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.gallery.ui.navigation.MainGraph
import com.example.gallery.ui.screen.HomeScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ryan.filemanager.ui.theme.GalleryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: MainActivityViewModel by viewModels()

    private val preTiramisuPermissions = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val tiramisuPermissions = listOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO
    )

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryTheme(darkTheme = viewModel.darkTheme) {
                val navController = rememberNavController()
                
                val storagePermissionState = rememberMultiplePermissionsState(
                    permissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                        preTiramisuPermissions else tiramisuPermissions,
                    onPermissionsResult = {
                        it.forEach { (_, granted) ->
                            if (!granted) {
                                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }
                )

                if (!storagePermissionState.allPermissionsGranted) {
                    // Request permissions
                    LaunchedEffect(true) {
                        storagePermissionState.launchMultiplePermissionRequest()
                    }
                } else {
                    // Screen content
                    MainGraph(navController = navController, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}