package com.hacybeyker.allset.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
data class DemoGroup(val demoId: String) : NavKey

@Serializable
data class ComingSoon(val demoId: String) : NavKey

@Serializable
data object BottomSheetDemo : NavKey

@Serializable
data object OnboardingDemo : NavKey

@Serializable
data object CarouselDemo : NavKey

@Serializable
data object ContactsDemo : NavKey

@Serializable
data object InboxDemo : NavKey

@Serializable
data object ShelvesDemo : NavKey

@Serializable
data object TabsDemo : NavKey

@Serializable
data object WebViewDemo : NavKey

@Serializable
data object KenBurnsDemo : NavKey

@Serializable
data object ZoomDemo : NavKey

@Serializable
data object SettingsDemo : NavKey

@Serializable
data object KeyboardDemo : NavKey

@Serializable
data object ClipboardDemo : NavKey

@Serializable
data object SharedElementsDemo : NavKey

@Serializable
data object ContentAnimationsDemo : NavKey

@Serializable
data object VerticalTransitionDemo : NavKey
