package com.hacybeyker.allset

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.hacybeyker.allset.core.data.settings.SettingsRepository
import com.hacybeyker.allset.core.data.settings.ThemeMode
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import com.hacybeyker.allset.feature.notifications.DemoNotifications
import com.hacybeyker.allset.navigation.AppNavHost
import com.hacybeyker.allset.navigation.Home
import com.hacybeyker.allset.navigation.NotificationsDemo

// Same scrims that enableEdgeToEdge uses by default for the three-button navigation bar.
private val LightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val settingsRepository = SettingsRepository(this)
        val initialBackStack = initialBackStack(intent)
        setContent {
            val settings by settingsRepository.settings.collectAsStateWithLifecycle(initialValue = null)
            // Nothing is drawn until the stored theme is read, so the app never flashes the wrong one.
            val currentSettings = settings ?: return@setContent
            val darkTheme = when (currentSettings.themeMode) {
                ThemeMode.System -> isSystemInDarkTheme()
                ThemeMode.Light -> false
                ThemeMode.Dark -> true
            }
            // The system bar icons must follow the app theme, which can differ from the system one.
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(LightScrim, DarkScrim) { darkTheme }
                )
                onDispose {}
            }
            AllSetTheme(darkTheme = darkTheme, dynamicColor = currentSettings.dynamicColor) {
                AppNavHost(initialBackStack = initialBackStack)
            }
        }
    }
}

/**
 * A notification of the demo opens it with Home underneath. Recents can relaunch the app with that
 * same intent, and then it starts on Home as usual.
 */
private fun initialBackStack(intent: Intent): List<NavKey> {
    val fromNotification = intent.getBooleanExtra(DemoNotifications.EXTRA_OPEN_NOTIFICATIONS_DEMO, false) &&
        (intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0
    return if (fromNotification) listOf(Home, NotificationsDemo) else listOf(Home)
}
