package com.hacybeyker.allset.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
data class DemoGroup(val demoId: String) : NavKey

@Serializable
data class DemoDetail(val demoId: String) : NavKey
