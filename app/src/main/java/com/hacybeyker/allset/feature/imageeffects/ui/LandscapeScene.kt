package com.hacybeyker.allset.feature.imageeffects.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.random.Random

private val SkyTop = Color(0xFF1A237E)
private val SkyBottom = Color(0xFFFF8A65)
private val Sun = Color(0xFFFFE082)
private val FarMountains = Color(0xFF7986CB)
private val NearMountains = Color(0xFF3949AB)
private val LakeTop = Color(0xFF283593)
private val LakeBottom = Color(0xFF0D1B4D)
private val Trees = Color(0xFF1B2A1F)

/** Sunset landscape drawn in code, with small details (stars, trees) that are worth zooming into. */
@Composable
fun LandscapeScene(modifier: Modifier = Modifier) {
    // Positions as fractions of the canvas, generated once with a fixed seed so they never move.
    val stars = remember {
        val random = Random(seed = 7)
        List(40) { Offset(random.nextFloat(), random.nextFloat() * 0.35f) }
    }
    Canvas(modifier = modifier) {
        val horizon = size.height * 0.7f
        drawRect(brush = Brush.verticalGradient(listOf(SkyTop, SkyBottom), endY = horizon), size = Size(size.width, horizon))
        stars.forEach { star ->
            drawCircle(
                color = Color.White.copy(alpha = 0.8f),
                radius = size.minDimension * 0.004f,
                center = Offset(star.x * size.width, star.y * size.height)
            )
        }
        drawCircle(color = Sun, radius = size.minDimension * 0.11f, center = Offset(size.width * 0.68f, size.height * 0.5f))
        drawMountains(
            color = FarMountains,
            peaks = listOf(0f to 0.6f, 0.18f to 0.42f, 0.36f to 0.56f, 0.55f to 0.38f, 0.78f to 0.58f, 1f to 0.46f),
            baseline = horizon
        )
        drawMountains(
            color = NearMountains,
            peaks = listOf(0f to 0.66f, 0.12f to 0.55f, 0.3f to 0.68f, 0.46f to 0.52f, 0.66f to 0.69f, 0.86f to 0.57f, 1f to 0.64f),
            baseline = horizon
        )
        drawRect(
            brush = Brush.verticalGradient(listOf(LakeTop, LakeBottom), startY = horizon, endY = size.height),
            topLeft = Offset(0f, horizon),
            size = Size(size.width, size.height - horizon)
        )
        // Reflection of the sun on the water.
        repeat(5) { index ->
            val width = size.width * (0.16f - index * 0.025f)
            val y = horizon + size.height * (0.03f + index * 0.035f)
            drawLine(
                color = Sun.copy(alpha = 0.6f - index * 0.1f),
                start = Offset(size.width * 0.68f - width / 2, y),
                end = Offset(size.width * 0.68f + width / 2, y),
                strokeWidth = size.minDimension * 0.008f
            )
        }
        drawTrees(horizon = horizon)
    }
}

private fun DrawScope.drawMountains(color: Color, peaks: List<Pair<Float, Float>>, baseline: Float) {
    val path = Path().apply {
        moveTo(0f, baseline)
        peaks.forEach { (x, y) -> lineTo(x * size.width, y * size.height) }
        lineTo(size.width, baseline)
        close()
    }
    drawPath(path = path, color = color)
}

private fun DrawScope.drawTrees(horizon: Float) {
    val treeHeight = size.height * 0.06f
    val treeWidth = treeHeight * 0.5f
    listOf(0.04f, 0.09f, 0.13f, 0.2f, 0.84f, 0.89f, 0.95f).forEach { fraction ->
        val x = fraction * size.width
        val path = Path().apply {
            moveTo(x, horizon - treeHeight)
            lineTo(x + treeWidth / 2, horizon)
            lineTo(x - treeWidth / 2, horizon)
            close()
        }
        drawPath(path = path, color = Trees)
    }
}
