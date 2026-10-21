package com.hacybeyker.allset.feature.imageeffects.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import kotlinx.coroutines.launch

private const val ButtonZoomFactor = 1.5f

// The minus icon isn't part of material-icons-core, so it is built in code with the same path.
private val ZoomOutIcon: ImageVector = materialIcon(name = "Filled.Remove") {
    materialPath {
        moveTo(19f, 13f)
        horizontalLineTo(5f)
        verticalLineToRelative(-2f)
        horizontalLineToRelative(14f)
        verticalLineToRelative(2f)
        close()
    }
}

@Composable
fun ZoomScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val zoomState = rememberZoomState()
    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.image_effects_zoom_title), onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.image_effects_zoom_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LandscapeScene(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .zoomable(zoomState)
            )
            // Buttons offer the same zoom without gestures, for accessibility services and keyboards.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(onClick = { scope.launch { zoomState.zoomBy(1f / ButtonZoomFactor) } }) {
                    Icon(imageVector = ZoomOutIcon, contentDescription = stringResource(R.string.image_effects_zoom_out))
                }
                Text(
                    text = stringResource(R.string.image_effects_zoom_level, zoomState.scale),
                    style = MaterialTheme.typography.titleMedium
                )
                FilledTonalIconButton(onClick = { scope.launch { zoomState.zoomBy(ButtonZoomFactor) } }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.image_effects_zoom_in))
                }
                FilledTonalIconButton(onClick = { scope.launch { zoomState.reset() } }) {
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = stringResource(R.string.image_effects_zoom_reset))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ZoomScreenPreview() {
    AllSetTheme {
        ZoomScreen(onBack = {})
    }
}
