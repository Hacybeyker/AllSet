package com.hacybeyker.allset.feature.spotlight.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R

private val HolePadding = 8.dp
private val ScrimColor = Color.Black.copy(alpha = 0.72f)

/** One step of the tutorial: the element registered with [targetKey] and what to say about it. */
data class SpotlightStep(val targetKey: String, @StringRes val title: Int, @StringRes val description: Int)

/** Which step is showing, and where each registered element is on screen. */
@Stable
class SpotlightState internal constructor(private val stepCount: Int, initialStep: Int) {
    var currentStep by mutableIntStateOf(initialStep)
        private set
    val isActive: Boolean get() = currentStep in 0 until stepCount
    internal val targets = mutableStateMapOf<String, Rect>()

    fun start() {
        currentStep = 0
    }

    fun next() {
        currentStep = if (currentStep + 1 < stepCount) currentStep + 1 else -1
    }

    fun finish() {
        currentStep = -1
    }

    internal companion object {
        fun saver(stepCount: Int) = Saver<SpotlightState, Int>(
            save = { it.currentStep },
            restore = { SpotlightState(stepCount, it) }
        )
    }
}

@Composable
fun rememberSpotlightState(stepCount: Int): SpotlightState =
    rememberSaveable(stepCount, saver = SpotlightState.saver(stepCount)) { SpotlightState(stepCount, -1) }

/** Registers this element so the tutorial can highlight it under [key]. */
fun Modifier.spotlightTarget(state: SpotlightState, key: String): Modifier =
    onGloballyPositioned { coordinates -> state.targets[key] = coordinates.boundsInRoot() }

/**
 * Draws [content] and, while [state] is active, a scrim over it with a hole around the element of
 * the current step and a card that explains it.
 */
@Composable
fun SpotlightHost(
    state: SpotlightState,
    steps: List<SpotlightStep>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BackHandler(enabled = state.isActive) { state.finish() }
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                // TalkBack only reaches the tutorial card while the tutorial is showing.
                .semantics { if (state.isActive) hideFromAccessibility() }
        ) {
            content()
        }
        if (state.isActive) {
            SpotlightOverlay(state = state, step = steps[state.currentStep], stepCount = steps.size)
        }
    }
}

@Composable
private fun SpotlightOverlay(state: SpotlightState, step: SpotlightStep, stepCount: Int) {
    val density = LocalDensity.current
    var overlayOrigin by remember { mutableStateOf(Offset.Zero) }
    var overlayHeight by remember { mutableFloatStateOf(0f) }
    val target = state.targets[step.targetKey]
    val padding = with(density) { HolePadding.toPx() }
    // Target bounds are in root coordinates; the overlay draws in its own.
    val hole = target?.translate(-overlayOrigin)?.inflate(padding) ?: Rect.Zero
    val animatedHole by animateRectAsState(targetValue = hole, label = "spotlightHole")
    val cardBelowHole = target == null || hole.center.y < overlayHeight / 2

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                overlayOrigin = coordinates.boundsInRoot().topLeft
                overlayHeight = coordinates.size.height.toFloat()
            }
            // Taps anywhere move to the next step, and nothing underneath receives them.
            .pointerInput(state) { detectTapGestures { state.next() } }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                // Offscreen so BlendMode.Clear only cuts the scrim, not what is drawn below it.
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        ) {
            drawRect(color = ScrimColor)
            if (animatedHole != Rect.Zero) {
                val radius = minOf(animatedHole.width, animatedHole.height) / 2
                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = animatedHole.topLeft,
                    size = animatedHole.size,
                    cornerRadius = CornerRadius(radius, radius),
                    blendMode = BlendMode.Clear
                )
            }
        }
        // Under the hole when the element is in the top half of the screen, above it otherwise.
        val holeTop = with(density) { animatedHole.top.toDp() }
        val holeBottom = with(density) { animatedHole.bottom.toDp() }
        val height = with(density) { overlayHeight.toDp() }
        SpotlightCard(
            step = step,
            stepIndex = state.currentStep,
            stepCount = stepCount,
            onNext = state::next,
            onSkip = state::finish,
            modifier = Modifier
                .align(if (cardBelowHole) Alignment.TopCenter else Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .padding(
                    top = if (cardBelowHole) holeBottom + 16.dp else 0.dp,
                    bottom = if (cardBelowHole) 0.dp else (height - holeTop + 16.dp).coerceAtLeast(0.dp)
                )
        )
    }
}

@Composable
private fun SpotlightCard(
    step: SpotlightStep,
    stepIndex: Int,
    stepCount: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLastStep = stepIndex == stepCount - 1
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            // Announces each new step to TalkBack.
            .semantics { liveRegion = LiveRegionMode.Polite }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.spotlight_step, stepIndex + 1, stepCount),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = stringResource(step.title), style = MaterialTheme.typography.titleMedium)
            Text(
                text = stringResource(step.description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                if (!isLastStep) {
                    TextButton(onClick = onSkip) {
                        Text(text = stringResource(R.string.spotlight_skip))
                    }
                }
                Button(onClick = onNext) {
                    Text(text = stringResource(if (isLastStep) R.string.spotlight_done else R.string.spotlight_next))
                }
            }
        }
    }
}
