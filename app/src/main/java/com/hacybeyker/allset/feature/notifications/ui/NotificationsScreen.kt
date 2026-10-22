package com.hacybeyker.allset.feature.notifications.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import com.hacybeyker.allset.feature.notifications.DemoNotifications
import com.hacybeyker.allset.feature.notifications.SampleDownload

@Composable
fun NotificationsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    var canPost by remember { mutableStateOf(DemoNotifications.canPost(context)) }
    // Only known after asking: the system dialog is no longer shown and Settings is the only way.
    var permissionBlocked by rememberSaveable { mutableStateOf(false) }
    val downloadProgress by SampleDownload.progress.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { DemoNotifications.createChannels(context) }
    // The user can change the permission in Settings while the app is in the background.
    LifecycleResumeEffect(Unit) {
        canPost = DemoNotifications.canPost(context)
        onPauseOrDispose {}
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        canPost = DemoNotifications.canPost(context)
        // Denied with no rationale to show: the system won't show its dialog again.
        permissionBlocked = !granted && activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS)
    }
    val needsRuntimePermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !permissionBlocked

    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.notifications_title), onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DemoCard(
                title = R.string.notifications_permission_title,
                description = when {
                    canPost -> R.string.notifications_permission_granted
                    needsRuntimePermission -> R.string.notifications_permission_rationale
                    else -> R.string.notifications_permission_blocked
                }
            ) {
                if (!canPost) {
                    if (needsRuntimePermission) {
                        Button(onClick = { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }) {
                            Text(text = stringResource(R.string.notifications_allow))
                        }
                    } else {
                        Button(onClick = { context.openNotificationSettings() }) {
                            Text(text = stringResource(R.string.notifications_open_settings))
                        }
                    }
                }
            }
            DemoCard(title = R.string.notifications_send_title, description = R.string.notifications_send_description) {
                Button(onClick = { DemoNotifications.showMessage(context) }, enabled = canPost, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(R.string.notifications_send_message))
                }
                Button(onClick = { DemoNotifications.showPicture(context) }, enabled = canPost, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(R.string.notifications_send_picture))
                }
                Button(
                    onClick = { SampleDownload.start(context) },
                    enabled = canPost && downloadProgress == null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val progress = downloadProgress
                    Text(
                        text = if (progress == null) {
                            stringResource(R.string.notifications_send_download)
                        } else {
                            stringResource(R.string.notifications_download_progress, progress)
                        }
                    )
                }
            }
            DemoCard(title = R.string.notifications_channels_title, description = R.string.notifications_channels_description) {
                OutlinedButton(onClick = { context.openNotificationSettings() }) {
                    Text(text = stringResource(R.string.notifications_open_channels))
                }
            }
        }
    }
}

@Composable
private fun DemoCard(
    @StringRes title: Int,
    @StringRes description: Int,
    content: @Composable () -> Unit
) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = stringResource(title), style = MaterialTheme.typography.titleMedium)
            Text(
                text = stringResource(description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            content()
        }
    }
}

/** The app's notification settings, where the user can allow them or silence each channel. */
private fun Context.openNotificationSettings() {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    } else {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, "package:$packageName".toUri())
    }
    startActivity(intent)
}

@Preview(showBackground = true)
@Composable
private fun NotificationsScreenPreview() {
    AllSetTheme {
        NotificationsScreen(onBack = {})
    }
}
