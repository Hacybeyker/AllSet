package com.hacybeyker.allset.feature.catalog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import com.hacybeyker.allset.feature.catalog.Demo
import com.hacybeyker.allset.feature.catalog.DemoCatalog

@Composable
fun CatalogScreen(
    title: String,
    demos: List<Demo>,
    onDemoClick: (Demo) -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = title, onBack = onBack, actions = actions) }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(items = demos, key = { it.id }) { demo ->
                ListItem(
                    headlineContent = { Text(text = demo.name) },
                    supportingContent = { Text(text = demo.description) },
                    trailingContent = if (demo.children.isNotEmpty()) {
                        { Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null) }
                    } else {
                        null
                    },
                    modifier = Modifier.clickable { onDemoClick(demo) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogScreenPreview() {
    AllSetTheme {
        CatalogScreen(title = "AllSet", demos = DemoCatalog.demos.filter { it.show }, onDemoClick = {})
    }
}
