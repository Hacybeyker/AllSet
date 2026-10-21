package com.hacybeyker.allset

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
import com.hacybeyker.allset.core.data.settings.SettingsRepository
import com.hacybeyker.allset.core.data.settings.ThemeMode
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import com.hacybeyker.allset.navigation.AppNavHost

// Same scrims that enableEdgeToEdge uses by default for the three-button navigation bar.
private val LightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val settingsRepository = SettingsRepository(this)
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
                AppNavHost()
            }
        }
    }
}
