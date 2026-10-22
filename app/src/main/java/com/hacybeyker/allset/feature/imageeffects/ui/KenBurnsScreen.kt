package com.hacybeyker.allset.feature.imageeffects.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme

@Composable
fun KenBurnsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var paused by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.image_effects_ken_burns_title), onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KenBurns(
                paused = paused,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f)
                    .clip(MaterialTheme.shapes.extraLarge)
            ) {
                LandscapeScene(modifier = Modifier.fillMaxSize())
            }
            Text(
                text = stringResource(R.string.image_effects_ken_burns_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = { paused = !paused }, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(
                        if (paused) R.string.image_effects_ken_burns_resume else R.string.image_effects_ken_burns_pause
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KenBurnsScreenPreview() {
    AllSetTheme {
        KenBurnsScreen(onBack = {})
    }
}
