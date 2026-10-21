package com.hacybeyker.allset.feature.clipboard.ui

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.PersistableBundle
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import kotlinx.coroutines.launch

private const val PromoCode = "ALLSET-2026"
private const val VerificationCode = "482913"

// Same key as ClipDescription.EXTRA_IS_SENSITIVE, which only exists as a constant from Android 13.
private const val ExtraIsSensitive = "android.content.extra.IS_SENSITIVE"

@Composable
fun ClipboardScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val resources = LocalResources.current
    val clipboard = LocalClipboard.current
    val snackbarHostState = remember { SnackbarHostState() }
    val pastedText = rememberTextFieldState()

    val showMessage: (String) -> Unit = { message ->
        scope.launch { snackbarHostState.showSnackbar(message) }
    }
    val copy: (text: String, sensitive: Boolean) -> Unit = { text, sensitive ->
        scope.launch {
            val clipData = ClipData.newPlainText(resources.getString(R.string.clipboard_label), text)
            if (sensitive) {
                clipData.description.extras = PersistableBundle().apply { putBoolean(ExtraIsSensitive, true) }
            }
            clipboard.setClipEntry(ClipEntry(clipData))
            // From Android 13 the system confirms every copy, so a second message would be a duplicate.
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                showMessage(resources.getString(R.string.clipboard_copied))
            }
        }
    }
    // The clipboard is only read when the user asks to paste: Android shows a notice on every read.
    val paste: () -> Unit = {
        scope.launch {
            val clipData = clipboard.getClipEntry()?.clipData
            val text = clipData
                ?.takeIf { it.itemCount > 0 && it.description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) }
                ?.getItemAt(0)
                ?.coerceToText(context)
                ?.toString()
            if (text.isNullOrEmpty()) {
                showMessage(resources.getString(R.string.clipboard_empty))
            } else {
                pastedText.setTextAndPlaceCursorAtEnd(text)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.clipboard_title), onBack = onBack) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CopyCard(
                title = R.string.clipboard_copy_title,
                description = R.string.clipboard_copy_description,
                value = PromoCode,
                action = R.string.clipboard_copy,
                onCopy = { copy(PromoCode, false) }
            )
            CopyCard(
                title = R.string.clipboard_sensitive_title,
                description = R.string.clipboard_sensitive_description,
                value = VerificationCode,
                action = R.string.clipboard_copy_sensitive,
                onCopy = { copy(VerificationCode, true) }
            )
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = stringResource(R.string.clipboard_paste_title), style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = stringResource(R.string.clipboard_paste_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        state = pastedText,
                        label = { Text(text = stringResource(R.string.clipboard_pasted_text)) },
                        lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 2, maxHeightInLines = 4),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = paste) {
                            Text(text = stringResource(R.string.clipboard_paste))
                        }
                        OutlinedButton(onClick = { pastedText.setTextAndPlaceCursorAtEnd("") }) {
                            Text(text = stringResource(R.string.clipboard_clear))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CopyCard(
    @StringRes title: Int,
    @StringRes description: Int,
    value: String,
    @StringRes action: Int,
    onCopy: () -> Unit
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = FontFamily.Monospace
                )
                Button(onClick = onCopy) {
                    Text(text = stringResource(action))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClipboardScreenPreview() {
    AllSetTheme {
        ClipboardScreen(onBack = {})
    }
}
