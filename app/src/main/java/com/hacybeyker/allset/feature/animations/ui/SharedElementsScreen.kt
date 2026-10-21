package com.hacybeyker.allset.feature.animations.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme

private data class Destination(
    val id: Int,
    @StringRes val name: Int,
    @StringRes val description: Int,
    val colors: List<Color>
)

private val destinations = listOf(
    Destination(1, R.string.animations_place_mountain, R.string.animations_place_mountain_description, listOf(Color(0xFF5C6BC0), Color(0xFF283593))),
    Destination(2, R.string.animations_place_forest, R.string.animations_place_forest_description, listOf(Color(0xFF43A047), Color(0xFF1B5E20))),
    Destination(3, R.string.animations_place_coast, R.string.animations_place_coast_description, listOf(Color(0xFF29B6F6), Color(0xFF01579B))),
    Destination(4, R.string.animations_place_desert, R.string.animations_place_desert_description, listOf(Color(0xFFFFB74D), Color(0xFFE65100))),
    Destination(5, R.string.animations_place_lake, R.string.animations_place_lake_description, listOf(Color(0xFF26A69A), Color(0xFF004D40))),
    Destination(6, R.string.animations_place_volcano, R.string.animations_place_volcano_description, listOf(Color(0xFFEF5350), Color(0xFFB71C1C)))
)

@Composable
fun SharedElementsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var selectedId by rememberSaveable { mutableStateOf<Int?>(null) }
    val closeDetail = { selectedId = null }

    // Back closes the detail first, running the shared transition in reverse.
    BackHandler(enabled = selectedId != null, onBack = closeDetail)

    Scaffold(
        modifier = modifier,
        topBar = {
            AllSetTopAppBar(
                title = stringResource(R.string.animations_shared_title),
                onBack = { if (selectedId != null) closeDetail() else onBack() }
            )
        }
    ) { innerPadding ->
        SharedTransitionLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(targetState = selectedId, label = "sharedElements") { id ->
                val destination = destinations.firstOrNull { it.id == id }
                if (destination == null) {
                    DestinationGrid(onClick = { selectedId = it.id }, animatedVisibilityScope = this@AnimatedContent)
                } else {
                    DestinationDetail(destination = destination, onClose = closeDetail, animatedVisibilityScope = this@AnimatedContent)
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.DestinationGrid(
    onClick: (Destination) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = destinations, key = { it.id }) { destination ->
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .clickable { onClick(destination) }
            ) {
                Box(
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "image-${destination.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(Brush.verticalGradient(destination.colors))
                )
                Text(
                    text = stringResource(destination.name),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "title-${destination.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.DestinationDetail(
    destination: Destination,
    onClose: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = "image-${destination.id}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .fillMaxWidth()
                .height(280.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(Brush.verticalGradient(destination.colors))
        )
        Text(
            text = stringResource(destination.name),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(key = "title-${destination.id}"),
                animatedVisibilityScope = animatedVisibilityScope
            )
        )
        Text(
            text = stringResource(destination.description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedButton(onClick = onClose, modifier = Modifier.align(Alignment.End)) {
            Text(text = stringResource(R.string.animations_close))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SharedElementsScreenPreview() {
    AllSetTheme {
        SharedElementsScreen(onBack = {})
    }
}
