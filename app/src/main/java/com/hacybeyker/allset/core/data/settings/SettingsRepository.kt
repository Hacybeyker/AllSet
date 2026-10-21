package com.hacybeyker.allset.core.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

// Top level so the whole process shares a single DataStore instance for this file.
private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private val ThemeModeKey = stringPreferencesKey("theme_mode")
private val DynamicColorKey = booleanPreferencesKey("dynamic_color")

/** App preferences stored on the device with Preferences DataStore. */
class SettingsRepository(context: Context) {

    private val dataStore = context.applicationContext.settingsDataStore

    val settings: Flow<AppSettings> = dataStore.data
        // A corrupted or unreadable file falls back to the defaults instead of crashing the app.
        .catch { exception -> if (exception is IOException) emit(emptyPreferences()) else throw exception }
        .map { preferences ->
            AppSettings(
                themeMode = preferences[ThemeModeKey]
                    ?.let { name -> ThemeMode.entries.firstOrNull { it.name == name } }
                    ?: ThemeMode.System,
                dynamicColor = preferences[DynamicColorKey] ?: false
            )
        }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences -> preferences[ThemeModeKey] = themeMode.name }
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        dataStore.edit { preferences -> preferences[DynamicColorKey] = enabled }
    }
}
