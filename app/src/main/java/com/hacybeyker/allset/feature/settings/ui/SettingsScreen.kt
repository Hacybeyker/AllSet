package com.hacybeyker.allset.feature.settings.ui

import android.app.UiModeManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.data.settings.AppSettings
import com.hacybeyker.allset.core.data.settings.SettingsRepository
import com.hacybeyker.allset.core.data.settings.ThemeMode
import com.hacybeyker.allset.core.ui.browser.openInCustomTab
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import kotlinx.coroutines.launch

/** Also set in Play Console, under App content › Privacy policy. */
private const val PrivacyPolicyUrl = "https://hacybeyker.github.io/allset/privacidad/"

@Composable
fun SettingsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { SettingsRepository(context) }
    val settings by repository.settings.collectAsStateWithLifecycle(initialValue = AppSettings())
    val appVersion = remember { context.appVersionName() }
    val snackbarHostState = remember { SnackbarHostState() }
    val noBrowserMessage = stringResource(R.string.no_browser)

    SettingsContent(
        settings = settings,
        appVersion = appVersion,
        snackbarHostState = snackbarHostState,
        dynamicColorAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
        onThemeModeChange = { themeMode ->
            scope.launch {
                // Saved first: applying the night mode recreates the activity and cancels this scope.
                repository.setThemeMode(themeMode)
                context.applyApplicationNightMode(themeMode)
            }
        },
        onDynamicColorChange = { enabled -> scope.launch { repository.setDynamicColor(enabled) } },
        onPrivacyPolicyClick = {
            if (!openInCustomTab(context, PrivacyPolicyUrl.toUri())) {
                scope.launch { snackbarHostState.showSnackbar(noBrowserMessage) }
            }
        },
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
private fun SettingsContent(
    settings: AppSettings,
    appVersion: String,
    snackbarHostState: SnackbarHostState,
    dynamicColorAvailable: Boolean,
    onThemeModeChange: (ThemeMode) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.settings_title), onBack = onBack) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            item(key = "theme-title") { SectionTitle(text = stringResource(R.string.settings_theme)) }
            item(key = "theme-options") {
                Column(modifier = Modifier.selectableGroup()) {
                    ThemeMode.entries.forEach { themeMode ->
                        val selected = settings.themeMode == themeMode
                        ListItem(
                            headlineContent = { Text(text = stringResource(themeMode.label())) },
                            leadingContent = { RadioButton(selected = selected, onClick = null) },
                            modifier = Modifier.selectable(
                                selected = selected,
                                role = Role.RadioButton,
                                onClick = { onThemeModeChange(themeMode) }
                            )
                        )
                    }
                }
            }
            item(key = "color-title") { SectionTitle(text = stringResource(R.string.settings_color)) }
            item(key = "dynamic-color") {
                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.settings_dynamic_color)) },
                    supportingContent = {
                        Text(
                            text = stringResource(
                                if (dynamicColorAvailable) {
                                    R.string.settings_dynamic_color_description
                                } else {
                                    R.string.settings_dynamic_color_unavailable
                                }
                            )
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = settings.dynamicColor && dynamicColorAvailable,
                            onCheckedChange = null,
                            enabled = dynamicColorAvailable
                        )
                    },
                    modifier = Modifier.toggleable(
                        value = settings.dynamicColor && dynamicColorAvailable,
                        enabled = dynamicColorAvailable,
                        role = Role.Switch,
                        onValueChange = onDynamicColorChange
                    )
                )
            }
            item(key = "about-title") { SectionTitle(text = stringResource(R.string.settings_about)) }
            item(key = "privacy-policy") {
                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.settings_privacy_policy)) },
                    supportingContent = { Text(text = stringResource(R.string.settings_privacy_policy_description)) },
                    modifier = Modifier.clickable(onClick = onPrivacyPolicyClick)
                )
            }
            item(key = "version") {
                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.settings_version)) },
                    supportingContent = { Text(text = appVersion) }
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@StringRes
private fun ThemeMode.label(): Int = when (this) {
    ThemeMode.System -> R.string.settings_theme_system
    ThemeMode.Light -> R.string.settings_theme_light
    ThemeMode.Dark -> R.string.settings_theme_dark
}

/**
 * From Android 12 the system can keep a night mode per app, so the window background, the
 * WebView and the system UI match the theme chosen here. Older versions only theme the Compose UI.
 */
private fun Context.applyApplicationNightMode(themeMode: ThemeMode) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
    val nightMode = when (themeMode) {
        ThemeMode.System -> UiModeManager.MODE_NIGHT_AUTO
        ThemeMode.Light -> UiModeManager.MODE_NIGHT_NO
        ThemeMode.Dark -> UiModeManager.MODE_NIGHT_YES
    }
    getSystemService(UiModeManager::class.java).setApplicationNightMode(nightMode)
}

private fun Context.appVersionName(): String {
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(packageName, 0)
    }
    return packageInfo.versionName.orEmpty()
}

@Preview(showBackground = true)
@Composable
private fun SettingsContentPreview() {
    AllSetTheme {
        SettingsContent(
            settings = AppSettings(themeMode = ThemeMode.Dark),
            appVersion = "7.4.0",
            snackbarHostState = remember { SnackbarHostState() },
            dynamicColorAvailable = true,
            onThemeModeChange = {},
            onDynamicColorChange = {},
            onPrivacyPolicyClick = {},
            onBack = {}
        )
    }
}
