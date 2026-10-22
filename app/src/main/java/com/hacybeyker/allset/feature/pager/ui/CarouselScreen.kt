package com.hacybeyker.allset.feature.pager.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselItemScope
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import kotlinx.coroutines.delay

private data class Banner(@StringRes val title: Int, @StringRes val subtitle: Int, val colors: List<Color>)

private data class Place(@StringRes val name: Int, val colors: List<Color>)

private val banners = listOf(
    Banner(
        R.string.carousel_banner_season_title,
        R.string.carousel_banner_season_subtitle,
        listOf(Color(0xFF6200EE), Color(0xFF3700B3))
    ),
    Banner(
        R.string.carousel_banner_new_title,
        R.string.carousel_banner_new_subtitle,
        listOf(Color(0xFF018786), Color(0xFF004D40))
    ),
    Banner(
        R.string.carousel_banner_for_you_title,
        R.string.carousel_banner_for_you_subtitle,
        listOf(Color(0xFFC2185B), Color(0xFF6A1B9A))
    )
)

private val places = listOf(
    Place(R.string.carousel_place_mountain, listOf(Color(0xFF5C6BC0), Color(0xFF283593))),
    Place(R.string.carousel_place_forest, listOf(Color(0xFF43A047), Color(0xFF1B5E20))),
    Place(R.string.carousel_place_coast, listOf(Color(0xFF29B6F6), Color(0xFF01579B))),
    Place(R.string.carousel_place_desert, listOf(Color(0xFFFFB74D), Color(0xFFE65100))),
    Place(R.string.carousel_place_lake, listOf(Color(0xFF26A69A), Color(0xFF004D40))),
    Place(R.string.carousel_place_volcano, listOf(Color(0xFFEF5350), Color(0xFFB71C1C)))
)

private const val BannerAutoScrollMillis = 3_000L

private val CarouselHeight = 220.dp
private val CarouselItemWidth = 186.dp
private val CarouselContentPadding = PaddingValues(horizontal = 16.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.carousel_title), onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CarouselSection(
                title = R.string.carousel_banner_section,
                description = R.string.carousel_banner_description
            ) {
                AutoScrollBanner()
            }
            CarouselSection(
                title = R.string.carousel_multi_browse_section,
                description = R.string.carousel_multi_browse_description
            ) {
                HorizontalMultiBrowseCarousel(
                    state = rememberCarouselState { places.size },
                    preferredItemWidth = CarouselItemWidth,
                    itemSpacing = 8.dp,
                    contentPadding = CarouselContentPadding,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(CarouselHeight)
                ) { index ->
                    PlaceCard(place = places[index])
                }
            }
            CarouselSection(
                title = R.string.carousel_hero_section,
                description = R.string.carousel_hero_description
            ) {
                HorizontalCenteredHeroCarousel(
                    state = rememberCarouselState { places.size },
                    itemSpacing = 8.dp,
                    contentPadding = CarouselContentPadding,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(CarouselHeight)
                ) { index ->
                    PlaceCard(place = places[index])
                }
            }
            CarouselSection(
                title = R.string.carousel_uncontained_section,
                description = R.string.carousel_uncontained_description
            ) {
                HorizontalUncontainedCarousel(
                    state = rememberCarouselState { places.size },
                    itemWidth = CarouselItemWidth,
                    itemSpacing = 8.dp,
                    contentPadding = CarouselContentPadding,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(CarouselHeight)
                ) { index ->
                    PlaceCard(place = places[index])
                }
            }
        }
    }
}

@Composable
private fun AutoScrollBanner() {
    val pagerState = rememberPagerState(pageCount = { banners.size })
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    // Advances on its own; dragging pauses it and restarts the countdown when released.
    LaunchedEffect(isDragged) {
        if (isDragged) return@LaunchedEffect
        while (true) {
            delay(BannerAutoScrollMillis)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % banners.size)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            BannerCard(banner = banners[page])
        }
        PagerIndicator(pageCount = banners.size, currentPage = pagerState.currentPage)
    }
}

@Composable
private fun BannerCard(banner: Banner) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(Brush.linearGradient(banner.colors))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(
                text = stringResource(banner.title),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Text(
                text = stringResource(banner.subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarouselItemScope.PlaceCard(place: Place) {
    val drawInfo = carouselItemDrawInfo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .maskClip(MaterialTheme.shapes.extraLarge)
            .background(Brush.verticalGradient(place.colors))
    ) {
        Text(
            text = stringResource(place.name),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .graphicsLayer {
                    // Only the larger items show the label, as items shrink toward the edges.
                    val range = drawInfo.maxSize - drawInfo.minSize
                    alpha = if (range > 0f) ((drawInfo.size - drawInfo.minSize) / range).coerceIn(0f, 1f) else 1f
                }
        )
    }
}

@Composable
private fun CarouselSection(
    @StringRes title: Int,
    @StringRes description: Int,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = stringResource(title), style = MaterialTheme.typography.titleMedium)
            Text(
                text = stringResource(description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun CarouselScreenPreview() {
    AllSetTheme {
        CarouselScreen(onBack = {})
    }
}
