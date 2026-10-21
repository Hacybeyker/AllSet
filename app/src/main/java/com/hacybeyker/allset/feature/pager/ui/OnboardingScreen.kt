package com.hacybeyker.allset.feature.pager.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import kotlin.math.absoluteValue
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val icon: ImageVector,
    @StringRes val title: Int,
    @StringRes val description: Int
)

private val onboardingPages = listOf(
    OnboardingPage(Icons.Filled.Search, R.string.onboarding_explore_title, R.string.onboarding_explore_description),
    OnboardingPage(Icons.Filled.Build, R.string.onboarding_learn_title, R.string.onboarding_learn_description),
    OnboardingPage(Icons.Filled.CheckCircle, R.string.onboarding_apply_title, R.string.onboarding_apply_description)
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val isLastPage = pagerState.currentPage == onboardingPages.lastIndex

    // Back goes to the previous page before leaving the onboarding.
    BackHandler(enabled = pagerState.currentPage > 0) {
        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
    }

    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (!isLastPage) {
                    TextButton(onClick = onFinish) {
                        Text(text = stringResource(R.string.onboarding_skip))
                    }
                }
            }
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                OnboardingPageContent(
                    page = onboardingPages[page],
                    // Fades and shrinks the pages being swiped away, based on their offset from the center.
                    modifier = Modifier.graphicsLayer {
                        val progress = 1f - pagerState.getOffsetDistanceInPages(page).absoluteValue.coerceIn(0f, 1f)
                        alpha = lerp(0.3f, 1f, progress)
                        scaleX = lerp(0.8f, 1f, progress)
                        scaleY = lerp(0.8f, 1f, progress)
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PagerIndicator(pageCount = onboardingPages.size, currentPage = pagerState.currentPage)
                Button(
                    onClick = {
                        if (isLastPage) {
                            onFinish()
                        } else {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        }
                    }
                ) {
                    Text(text = stringResource(if (isLastPage) R.string.onboarding_start else R.string.onboarding_next))
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(160.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(72.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(page.title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(page.description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    AllSetTheme {
        OnboardingScreen(onFinish = {})
    }
}
