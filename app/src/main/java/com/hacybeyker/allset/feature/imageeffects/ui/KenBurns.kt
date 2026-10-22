package com.hacybeyker.allset.feature.imageeffects.ui

import android.provider.Settings
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import kotlin.random.Random
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val MinScale = 1.15f
private const val MaxScale = 1.45f

/**
 * Slowly zooms and pans [content] toward random targets without ever showing its edges. It stays
 * still when [paused] is true or when the system has animations turned off.
 */
@Composable
fun KenBurns(
    modifier: Modifier = Modifier,
    paused: Boolean = false,
    moveDurationMillis: Int = 7_000,
    content: @Composable BoxScope.() -> Unit
) {
    val context = LocalContext.current
    val animationsEnabled = remember {
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) > 0f
    }
    val scale = remember { Animatable(MinScale) }
    // Offsets are fractions of the size, so they work for any container dimension.
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(paused, animationsEnabled) {
        if (paused || !animationsEnabled) return@LaunchedEffect
        while (true) {
            val targetScale = Random.nextFloat() * (MaxScale - MinScale) + MinScale
            // The content grows by (scale - 1) / 2 on each side, which is how far it can move.
            val maxShift = (targetScale - 1f) / 2f
            val spec = tween<Float>(durationMillis = moveDurationMillis, easing = LinearEasing)
            coroutineScope {
                launch { scale.animateTo(targetScale, spec) }
                launch { offsetX.animateTo((Random.nextFloat() * 2f - 1f) * maxShift, spec) }
                launch { offsetY.animateTo((Random.nextFloat() * 2f - 1f) * maxShift, spec) }
            }
        }
    }

    Box(modifier = modifier.clipToBounds()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    translationX = offsetX.value * size.width
                    translationY = offsetY.value * size.height
                },
            content = content
        )
    }
}
