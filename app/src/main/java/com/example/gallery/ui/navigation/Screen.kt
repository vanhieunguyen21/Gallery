package com.example.gallery.ui.navigation

import com.example.gallery.model.Bucket
import com.google.gson.Gson

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home")
    object BucketScreen : Screen("bucket/{bucket}") {
        fun routeWithArgs(bucket: Bucket): String {
            return "bucket/${Gson().toJson(bucket)}"
        }
    }
}
