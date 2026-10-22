package com.hacybeyker.allset.feature.animations.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme

/** Its enter and exit transitions are declared in the metadata of its navigation entry. */
@Composable
fun VerticalTransitionScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.animations_transition_title), onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.animations_transition_description),
                style = MaterialTheme.typography.bodyLarge
            )
            Button(onClick = onBack) {
                Text(text = stringResource(R.string.animations_close))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerticalTransitionScreenPreview() {
    AllSetTheme {
        VerticalTransitionScreen(onBack = {})
    }
}
