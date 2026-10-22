package com.hacybeyker.allset.feature.spotlight.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.components.Avatar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme

private const val SearchTarget = "search"
private const val FiltersTarget = "filters"
private const val ComposeTarget = "compose"

private val tutorialSteps = listOf(
    SpotlightStep(SearchTarget, R.string.spotlight_search_title, R.string.spotlight_search_description),
    SpotlightStep(FiltersTarget, R.string.spotlight_filters_title, R.string.spotlight_filters_description),
    SpotlightStep(ComposeTarget, R.string.spotlight_compose_title, R.string.spotlight_compose_description)
)

private enum class InboxFilter(@StringRes val label: Int) {
    All(R.string.spotlight_filter_all),
    Unread(R.string.spotlight_filter_unread),
    Important(R.string.spotlight_filter_important)
}

private val senders = listOf("Lucía Mendoza", "Diego Flores", "Camila Rojas", "Pablo Reyes", "Valeria Ríos")

@Composable
fun SpotlightScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val spotlight = rememberSpotlightState(stepCount = tutorialSteps.size)
    var tutorialShown by rememberSaveable { mutableStateOf(false) }
    var selectedFilter by rememberSaveable { mutableStateOf(InboxFilter.All) }

    // Like a first-run tutorial: it starts by itself once, and can be replayed with the button.
    LaunchedEffect(Unit) {
        if (!tutorialShown) {
            tutorialShown = true
            spotlight.start()
        }
    }

    SpotlightHost(state = spotlight, steps = tutorialSteps, modifier = modifier) {
        Scaffold(
            topBar = {
                AllSetTopAppBar(
                    title = stringResource(R.string.spotlight_title),
                    onBack = onBack,
                    actions = {
                        IconButton(onClick = {}, modifier = Modifier.spotlightTarget(spotlight, SearchTarget)) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(R.string.spotlight_search_title))
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text(text = stringResource(R.string.spotlight_compose_title)) },
                    icon = { Icon(imageVector = Icons.Filled.Edit, contentDescription = null) },
                    onClick = {},
                    modifier = Modifier.spotlightTarget(spotlight, ComposeTarget)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.spotlight_intro),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                OutlinedButton(onClick = spotlight::start, modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(text = stringResource(R.string.spotlight_replay))
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .spotlightTarget(spotlight, FiltersTarget),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InboxFilter.entries.forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(text = stringResource(filter.label)) }
                        )
                    }
                }
                senders.forEach { sender ->
                    ListItem(
                        headlineContent = { Text(text = sender) },
                        supportingContent = { Text(text = stringResource(R.string.spotlight_sample_message)) },
                        leadingContent = { Avatar(name = sender) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpotlightScreenPreview() {
    AllSetTheme {
        SpotlightScreen(onBack = {})
    }
}
