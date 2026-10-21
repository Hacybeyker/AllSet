package com.hacybeyker.allset.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.hacybeyker.allset.R
import com.hacybeyker.allset.feature.animations.ui.ContentAnimationsScreen
import com.hacybeyker.allset.feature.animations.ui.SharedElementsScreen
import com.hacybeyker.allset.feature.animations.ui.VerticalTransitionScreen
import com.hacybeyker.allset.feature.bottomsheet.ui.BottomSheetScreen
import com.hacybeyker.allset.feature.catalog.Demo
import com.hacybeyker.allset.feature.catalog.DemoCatalog
import com.hacybeyker.allset.feature.catalog.ui.CatalogScreen
import com.hacybeyker.allset.feature.catalog.ui.ComingSoonScreen
import com.hacybeyker.allset.feature.clipboard.ui.ClipboardScreen
import com.hacybeyker.allset.feature.imageeffects.ui.KenBurnsScreen
import com.hacybeyker.allset.feature.imageeffects.ui.ZoomScreen
import com.hacybeyker.allset.feature.keyboard.ui.KeyboardScreen
import com.hacybeyker.allset.feature.lists.ui.ContactsScreen
import com.hacybeyker.allset.feature.lists.ui.InboxScreen
import com.hacybeyker.allset.feature.lists.ui.ShelvesScreen
import com.hacybeyker.allset.feature.pager.ui.CarouselScreen
import com.hacybeyker.allset.feature.pager.ui.OnboardingScreen
import com.hacybeyker.allset.feature.settings.ui.SettingsScreen
import com.hacybeyker.allset.feature.spotlight.ui.SpotlightScreen
import com.hacybeyker.allset.feature.tabs.ui.TabsScreen
import com.hacybeyker.allset.feature.webview.ui.WebViewScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Home)
    val onBack: () -> Unit = { backStack.removeLastOrNull() }
    val openDemo: (Demo) -> Unit = { demo ->
        backStack.add(
            when {
                demo.destination != null -> demo.destination
                demo.children.isNotEmpty() -> DemoGroup(demo.id)
                else -> ComingSoon(demo.id)
            }
        )
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
                    onDemoClick = openDemo,
                    actions = {
                        IconButton(onClick = { backStack.add(SettingsDemo) }) {
                            Icon(imageVector = Icons.Filled.Settings, contentDescription = stringResource(R.string.settings_title))
                        }
                    }
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
            entry<ComingSoon> { key ->
                ComingSoonScreen(demo = DemoCatalog.find(key.demoId), onBack = onBack)
            }
            entry<BottomSheetDemo> {
                BottomSheetScreen(onBack = onBack)
            }
            entry<OnboardingDemo> {
                OnboardingScreen(onFinish = onBack)
            }
            entry<CarouselDemo> {
                CarouselScreen(onBack = onBack)
            }
            entry<ContactsDemo> {
                ContactsScreen(onBack = onBack)
            }
            entry<InboxDemo> {
                InboxScreen(onBack = onBack)
            }
            entry<ShelvesDemo> {
                ShelvesScreen(onBack = onBack)
            }
            entry<TabsDemo> {
                TabsScreen(onBack = onBack)
            }
            entry<WebViewDemo> {
                WebViewScreen(onBack = onBack)
            }
            entry<KenBurnsDemo> {
                KenBurnsScreen(onBack = onBack)
            }
            entry<ZoomDemo> {
                ZoomScreen(onBack = onBack)
            }
            entry<SettingsDemo> {
                SettingsScreen(onBack = onBack)
            }
            entry<KeyboardDemo> {
                KeyboardScreen(onBack = onBack)
            }
            entry<ClipboardDemo> {
                ClipboardScreen(onBack = onBack)
            }
            entry<SharedElementsDemo> {
                SharedElementsScreen(onBack = onBack)
            }
            entry<ContentAnimationsDemo> {
                ContentAnimationsScreen(onBack = onBack)
            }
            // This entry declares its own transitions: it enters from the bottom and leaves the same
            // way, also while following the predictive back gesture.
            entry<VerticalTransitionDemo>(
                metadata = NavDisplay.transitionSpec {
                    slideInVertically { height -> height } togetherWith ExitTransition.KeepUntilTransitionsFinished
                } + NavDisplay.popTransitionSpec {
                    EnterTransition.None togetherWith slideOutVertically { height -> height }
                } + NavDisplay.predictivePopTransitionSpec { _ ->
                    EnterTransition.None togetherWith slideOutVertically { height -> height }
                }
            ) {
                VerticalTransitionScreen(onBack = onBack)
            }
            entry<SpotlightDemo> {
                SpotlightScreen(onBack = onBack)
            }
        }
    )
}
