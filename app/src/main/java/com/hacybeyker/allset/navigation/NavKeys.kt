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
