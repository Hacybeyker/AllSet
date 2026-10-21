package com.hacybeyker.allset.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.hacybeyker.allset.R
import com.hacybeyker.allset.feature.catalog.Demo
import com.hacybeyker.allset.feature.catalog.DemoCatalog
import com.hacybeyker.allset.feature.catalog.ui.CatalogScreen
import com.hacybeyker.allset.feature.catalog.ui.ComingSoonScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Home)
    val onBack: () -> Unit = { backStack.removeLastOrNull() }
    val openDemo: (Demo) -> Unit = { demo ->
        backStack.add(if (demo.children.isEmpty()) DemoDetail(demo.id) else DemoGroup(demo.id))
    }
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = onBack,
        entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
        entryProvider = entryProvider {
            entry<Home> {
                CatalogScreen(
                    title = stringResource(R.string.app_name),
                    demos = DemoCatalog.demos.filter { it.show },
                    onDemoClick = openDemo
                )
            }
            entry<DemoGroup> { key ->
                val demo = DemoCatalog.find(key.demoId)
                CatalogScreen(
                    title = demo.name,
                    demos = demo.children.filter { it.show },
                    onDemoClick = openDemo,
                    onBack = onBack
                )
            }
            entry<DemoDetail> { key ->
                ComingSoonScreen(demo = DemoCatalog.find(key.demoId), onBack = onBack)
            }
        }
    )
}
