package com.example.gallery.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gallery.R
import com.example.gallery.util.getBrokenThumbnail
import com.example.gallery.util.getImageThumbnail
import com.example.gallery.util.getVideoThumbnail
import com.example.gallery.model.Bucket
import com.example.gallery.model.Image
import com.example.gallery.model.Video
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun BucketItem(
    bucket: Bucket,
    thumbnail: Bitmap?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlideImage(
            imageModel = { thumbnail ?: R.drawable.ic_broken_image },
            modifier = modifier.aspectRatio(1f)
                .clip(RoundedCornerShape(15)),
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = bucket.name,
            style = MaterialTheme.typography.h6.copy(fontSize = 18.sp),
            color = MaterialTheme.colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}