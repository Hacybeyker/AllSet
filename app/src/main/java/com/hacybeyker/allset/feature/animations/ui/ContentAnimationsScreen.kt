package com.hacybeyker.allset.feature.animations.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.icons.AllSetIcons
import com.hacybeyker.allset.core.ui.theme.AllSetTheme

@Composable
fun ContentAnimationsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.animations_content_title), onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimationCard(title = R.string.animations_counter_title, description = R.string.animations_counter_description) {
                AnimatedCounter()
            }
            AnimationCard(title = R.string.animations_visibility_title, description = R.string.animations_visibility_description) {
                VisibilityToggle()
            }
            AnimationCard(title = R.string.animations_size_title, description = R.string.animations_size_description) {
                ExpandableText()
            }
            AnimationCard(title = R.string.animations_value_title, description = R.string.animations_value_description) {
                FavoriteToggle()
            }
            AnimationCard(title = R.string.animations_infinite_title, description = R.string.animations_infinite_description) {
                LiveIndicator()
            }
        }
    }
}

@Composable
private fun AnimationCard(
    @StringRes title: Int,
    @StringRes description: Int,
    content: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = stringResource(title), style = MaterialTheme.typography.titleMedium)
            Text(
                text = stringResource(description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            content()
        }
    }
}

/** The number slides up when it grows and down when it shrinks. */
@Composable
private fun AnimatedCounter() {
    var count by rememberSaveable { mutableIntStateOf(0) }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        FilledTonalIconButton(onClick = { count-- }) {
            Icon(imageVector = AllSetIcons.Remove, contentDescription = stringResource(R.string.animations_counter_decrease))
        }
        AnimatedContent(
            targetState = count,
            transitionSpec = {
                val direction = if (targetState > initialState) 1 else -1
                (slideInVertically { height -> direction * height } + fadeIn())
                    .togetherWith(slideOutVertically { height -> -direction * height } + fadeOut())
                    .using(SizeTransform(clip = false))
            },
            label = "counter"
        ) { value ->
            Text(text = value.toString(), style = MaterialTheme.typography.displaySmall)
        }
        FilledTonalIconButton(onClick = { count++ }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.animations_counter_increase))
        }
    }
}

@Composable
private fun VisibilityToggle() {
    var visible by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = visible, role = Role.Switch, onValueChange = { visible = it }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.animations_visibility_toggle), modifier = Modifier.weight(1f))
        Switch(checked = visible, onCheckedChange = null)
    }
    AnimatedVisibility(visible = visible, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
        Text(
            text = stringResource(R.string.animations_visibility_message),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(12.dp)
        )
    }
}

@Composable
private fun ExpandableText() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(modifier = Modifier.animateContentSize()) {
        Text(
            text = stringResource(R.string.animations_size_text),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (expanded) Int.MAX_VALUE else 2
        )
        TextButton(onClick = { expanded = !expanded }) {
            Text(text = stringResource(if (expanded) R.string.animations_size_less else R.string.animations_size_more))
        }
    }
}

/** Color and scale animate to their new values, with a spring that bounces a little. */
@Composable
private fun FavoriteToggle() {
    var favorite by rememberSaveable { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if (favorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "favoriteColor"
    )
    val scale by animateFloatAsState(
        targetValue = if (favorite) 1.3f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "favoriteScale"
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { favorite = !favorite }) {
            Icon(
                imageVector = if (favorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = stringResource(
                    if (favorite) R.string.animations_value_remove_favorite else R.string.animations_value_add_favorite
                ),
                tint = color,
                modifier = Modifier.scale(scale)
            )
        }
        Text(text = stringResource(if (favorite) R.string.animations_value_favorite else R.string.animations_value_not_favorite))
    }
}

@Composable
private fun LiveIndicator() {
    val transition = rememberInfiniteTransition(label = "live")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = 800), repeatMode = RepeatMode.Reverse),
        label = "liveAlpha"
    )
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .graphicsLayer { this.alpha = alpha }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.error)
        )
        Text(text = stringResource(R.string.animations_infinite_live), style = MaterialTheme.typography.labelLarge)
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentAnimationsScreenPreview() {
    AllSetTheme {
        ContentAnimationsScreen(onBack = {})
    }
}
