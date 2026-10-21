package com.hacybeyker.allset.feature.imageeffects.ui

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val DoubleTapScale = 2.5f
private const val AnimationMillis = 300

/** Zoom and pan of the content drawn with [zoomable]. Scale goes from 1 to [maxScale]. */
@Stable
class ZoomState(val maxScale: Float = 5f) {
    var scale by mutableFloatStateOf(1f)
        private set
    var offset by mutableStateOf(Offset.Zero)
        private set
    internal var size = IntSize.Zero

    /** Zooms by [zoom] around [centroid] and moves by [pan], keeping the content edges on screen. */
    internal fun transform(centroid: Offset, pan: Offset, zoom: Float) {
        val newScale = (scale * zoom).coerceIn(1f, maxScale)
        // Keeps the point under the fingers in the same place while the scale changes.
        val focus = centroid - center()
        val newOffset = focus * (1f - newScale / scale) + offset * (newScale / scale) + pan
        scale = newScale
        offset = clamp(newOffset, newScale)
    }

    /** Zooms in to [DoubleTapScale] around [tap], or back out when already zoomed. */
    suspend fun toggle(tap: Offset) {
        if (scale > 1f) {
            animateTo(1f, Offset.Zero)
        } else {
            animateTo(DoubleTapScale, clamp((tap - center()) * (1f - DoubleTapScale), DoubleTapScale))
        }
    }

    /** Zooms around the center, as used by the buttons that make zoom work without gestures. */
    suspend fun zoomBy(factor: Float) {
        val targetScale = (scale * factor).coerceIn(1f, maxScale)
        animateTo(targetScale, clamp(offset * (targetScale / scale), targetScale))
    }

    suspend fun reset() = animateTo(1f, Offset.Zero)

    private suspend fun animateTo(targetScale: Float, targetOffset: Offset) {
        val startScale = scale
        val startOffset = offset
        animate(0f, 1f, animationSpec = tween(AnimationMillis)) { fraction, _ ->
            scale = lerp(startScale, targetScale, fraction)
            offset = lerp(startOffset, targetOffset, fraction)
        }
    }

    private fun center() = Offset(size.width / 2f, size.height / 2f)

    private fun clamp(offset: Offset, scale: Float): Offset {
        val maxX = (scale - 1f) * size.width / 2f
        val maxY = (scale - 1f) * size.height / 2f
        return Offset(offset.x.coerceIn(-maxX, maxX), offset.y.coerceIn(-maxY, maxY))
    }
}

@Composable
fun rememberZoomState(maxScale: Float = 5f): ZoomState = remember(maxScale) { ZoomState(maxScale) }

/** Pinch to zoom around the fingers, drag to pan and double tap to toggle the zoom. */
fun Modifier.zoomable(state: ZoomState): Modifier = this
    .clipToBounds()
    .onSizeChanged { state.size = it }
    .pointerInput(state) {
        detectTransformGestures { centroid, pan, zoom, _ -> state.transform(centroid, pan, zoom) }
    }
    .pointerInput(state) {
        coroutineScope {
            detectTapGestures(onDoubleTap = { tap -> launch { state.toggle(tap) } })
        }
    }
    .graphicsLayer {
        scaleX = state.scale
        scaleY = state.scale
        translationX = state.offset.x
        translationY = state.offset.y
    }
