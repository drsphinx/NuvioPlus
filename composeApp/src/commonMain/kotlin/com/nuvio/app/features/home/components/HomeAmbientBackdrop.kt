package com.nuvio.app.features.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kmpalette.extensions.painter.rememberPainterDominantColorState

private const val ACCENT_BLEND_FRACTION = 0.48f

@Composable
internal fun HomeAmbientBackdrop(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    val background = MaterialTheme.colorScheme.background
    val painterColorState = rememberPainterDominantColorState(
        defaultColor = background,
        defaultOnColor = MaterialTheme.colorScheme.onBackground,
    )
    var sampledPainter by remember { mutableStateOf<Painter?>(null) }

    LaunchedEffect(imageUrl) {
        if (imageUrl.isNullOrBlank()) {
            sampledPainter = null
        }
    }
    LaunchedEffect(sampledPainter) {
        val painter = sampledPainter ?: return@LaunchedEffect
        runCatching { painterColorState.updateFrom(painter) }
    }

    val accentTarget = if (sampledPainter != null) {
        background.blendTowards(painterColorState.color, ACCENT_BLEND_FRACTION)
    } else {
        background
    }
    val accent by animateColorAsState(
        targetValue = accentTarget,
        animationSpec = tween(durationMillis = 420, easing = LinearOutSlowInEasing),
        label = "home_ambient_accent",
    )

    Box(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val widthPx = with(LocalDensity.current) { maxWidth.toPx() }
            val heightPx = with(LocalDensity.current) { maxHeight.toPx() }
            val glowRadius = maxOf(widthPx, heightPx) * 0.78f

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                accent.copy(alpha = 0.78f),
                                accent.copy(alpha = 0.32f),
                                Color.Transparent,
                            ),
                            center = Offset(widthPx * 0.5f, heightPx * 0.22f),
                            radius = glowRadius,
                        ),
                    ),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.00f to Color.Transparent,
                                0.42f to background.copy(alpha = 0.18f),
                                0.72f to background.copy(alpha = 0.62f),
                                1.00f to background.copy(alpha = 0.90f),
                            ),
                        ),
                    ),
            )
        }

        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .graphicsLayer { alpha = 0f },
                contentScale = ContentScale.Crop,
                onSuccess = { state -> sampledPainter = state.painter },
            )
        }
    }
}

private fun Color.blendTowards(target: Color, fraction: Float): Color {
    val clamped = fraction.coerceIn(0f, 1f)
    return Color(
        red = red + (target.red - red) * clamped,
        green = green + (target.green - green) * clamped,
        blue = blue + (target.blue - blue) * clamped,
        alpha = alpha + (target.alpha - alpha) * clamped,
    )
}
