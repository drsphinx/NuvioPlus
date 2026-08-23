package com.nuvio.app.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

private const val AMBIENT_IMAGE_SCALE = 1.28f
private val AMBIENT_BLUR = 48.dp

@Composable
internal fun HomeAmbientBackdrop(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    if (imageUrl.isNullOrBlank()) return

    val background = MaterialTheme.colorScheme.background
    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = AMBIENT_IMAGE_SCALE
                    scaleY = AMBIENT_IMAGE_SCALE
                }
                .blur(AMBIENT_BLUR),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            background.copy(alpha = 0.42f),
                            background.copy(alpha = 0.62f),
                            background.copy(alpha = 0.82f),
                            background.copy(alpha = 0.92f),
                        ),
                    ),
                ),
        )
    }
}
