package com.example.gym.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.Parameters
import coil.util.DebugLogger

@Composable
fun GymAsyncImage(
    model: Any?,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null
) {
    val context = LocalContext.current
    Surface(
        color = Color.Transparent,
        contentColor = Color.Transparent,
        shape = shape
    ) {
        val imageLoader = LocalContext.current.imageLoader.newBuilder()
            .logger(DebugLogger())
            .build()

        AsyncImage(
            imageLoader = imageLoader,
            modifier = modifier.clip(shape),
            model = ImageRequest.Builder(context)
                .data("https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/${model}")
                .decoderFactory(SvgDecoder.Factory())
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            filterQuality = FilterQuality.None
        )
    }
}