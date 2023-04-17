package com.example.gallery.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.gallery.R
import com.example.gallery.util.getImageThumbnail
import com.example.gallery.util.getVideoThumbnail
import com.example.gallery.model.Image
import com.example.gallery.model.Video
import com.example.gallery.util.getDurationString
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ImageItem(image: Image, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bmp = remember {
        getImageThumbnail(context, image, 300, 300)
    }
    ConstraintLayout(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxSize()
    ) {
        val star = createRef()
        GlideImage(
            imageModel = { bmp },
            modifier = Modifier.aspectRatio(1f),
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        )
        if (image.favorite) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier
                    .padding(4.dp)
                    .size(24.dp)
                    .constrainAs(star) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
        }
    }

}

@Composable
fun VideoItem(video: Video, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bmp = remember {
        getVideoThumbnail(context, video, 300, 300)
    }
    ConstraintLayout(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxSize()
    ) {
        val (duration, star) = createRefs()

        GlideImage(
            imageModel = { bmp },
            modifier = Modifier.aspectRatio(1f),
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        )
        Text(
            text = getDurationString(video.duration),
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .constrainAs(duration) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .padding(1.dp)
                .clip(RoundedCornerShape(15))
                .background(Color(0x75000000))
                .padding(vertical = 1.dp, horizontal = 4.dp),
            color = Color.White,
        )
        if (video.favorite) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier
                    .padding(4.dp)
                    .size(24.dp)
                    .constrainAs(star) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}