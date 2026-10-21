package com.hacybeyker.allset.feature.lists.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

private data class ShelfItem(val id: String, val label: String, val colors: List<Color>)

private data class Shelf(val id: String, @StringRes val title: Int, val items: List<ShelfItem>)

private val cities = listOf(
    "Lima", "Cusco", "Bogotá", "Quito", "Santiago", "Buenos Aires",
    "Montevideo", "Madrid", "Sevilla", "Ciudad de México", "La Paz", "Asunción"
)

private val gradients = listOf(
    listOf(Color(0xFF5C6BC0), Color(0xFF283593)),
    listOf(Color(0xFF26A69A), Color(0xFF004D40)),
    listOf(Color(0xFFEF5350), Color(0xFFB71C1C)),
    listOf(Color(0xFFAB47BC), Color(0xFF4A148C)),
    listOf(Color(0xFFFFA726), Color(0xFFE65100)),
    listOf(Color(0xFF29B6F6), Color(0xFF01579B))
)

private val shelves: List<Shelf> = listOf(
    "recommended" to R.string.lists_shelf_recommended,
    "popular" to R.string.lists_shelf_popular,
    "new" to R.string.lists_shelf_new,
    "weekend" to R.string.lists_shelf_weekend,
    "nearby" to R.string.lists_shelf_nearby,
    "classics" to R.string.lists_shelf_classics
).mapIndexed { shelfIndex, (id, title) ->
    Shelf(
        id = id,
        title = title,
        items = cities.indices.map { cityIndex ->
            // Rotates the cities and colors so each shelf looks different.
            val position = (cityIndex + shelfIndex * 5) % cities.size
            ShelfItem(
                id = "$id-$position",
                label = cities[position],
                colors = gradients[(position + shelfIndex) % gradients.size]
            )
        }
    )
}

@Composable
fun ShelvesScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.lists_shelves_title), onBack = onBack) }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item(key = "hint", contentType = "hint") {
                Text(
                    text = stringResource(R.string.lists_shelves_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            items(items = shelves, key = { it.id }, contentType = { "shelf" }) { shelf ->
                ShelfRow(shelf = shelf)
            }
        }
    }
}

@Composable
private fun ShelfRow(shelf: Shelf) {
    // Saved per row, so each row keeps its position when it scrolls off screen and comes back.
    val rowState = rememberLazyListState()
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(shelf.title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(
            state = rowState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = rowState, snapPosition = SnapPosition.Start),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = shelf.items, key = { it.id }) { item ->
                ShelfCard(item = item)
            }
        }
    }
}

@Composable
private fun ShelfCard(item: ShelfItem) {
    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 180.dp)
            .clip(MaterialTheme.shapes.large)
            .background(Brush.verticalGradient(item.colors))
    ) {
        Text(
            text = item.label,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShelvesScreenPreview() {
    AllSetTheme {
        ShelvesScreen(onBack = {})
    }
}
