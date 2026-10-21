package com.hacybeyker.allset.feature.keyboard.ui

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import kotlinx.coroutines.launch

private const val MinPasswordLength = 8
private const val MaxPhoneLength = 15

// Phone numbers only take digits, spaces and "+"; anything else is rejected as it is typed.
private val PhoneInputTransformation = InputTransformation.maxLength(MaxPhoneLength).then(
    InputTransformation {
        if (!asCharSequence().all { it.isDigit() || it == ' ' || it == '+' }) revertAllChanges()
    }
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KeyboardScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val name = rememberTextFieldState()
    val email = rememberTextFieldState()
    val phone = rememberTextFieldState()
    val password = rememberTextFieldState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    // Errors only show up after the first submit, so the form doesn't start in red.
    var showErrors by rememberSaveable { mutableStateOf(false) }

    val nameValid = name.text.isNotBlank()
    val emailValid = Patterns.EMAIL_ADDRESS.matcher(email.text).matches()
    val passwordValid = password.text.length >= MinPasswordLength

    val hideKeyboard: () -> Unit = {
        keyboardController?.hide()
        focusManager.clearFocus()
    }
    val submit: () -> Unit = {
        showErrors = true
        if (nameValid && emailValid && passwordValid) {
            hideKeyboard()
            showErrors = false
            scope.launch { snackbarHostState.showSnackbar(resources.getString(R.string.keyboard_form_sent)) }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.keyboard_title), onBack = onBack) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                // The navigation bar is already in innerPadding, so imePadding only adds the rest.
                .consumeWindowInsets(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.keyboard_intro),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            KeyboardStatus(visible = WindowInsets.isImeVisible)
            OutlinedTextField(
                state = name,
                label = { Text(text = stringResource(R.string.keyboard_name)) },
                isError = showErrors && !nameValid,
                supportingText = errorText(show = showErrors && !nameValid, text = R.string.keyboard_name_error),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentType = ContentType.PersonFullName }
            )
            OutlinedTextField(
                state = email,
                label = { Text(text = stringResource(R.string.keyboard_email)) },
                isError = showErrors && !emailValid,
                supportingText = errorText(show = showErrors && !emailValid, text = R.string.keyboard_email_error),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentType = ContentType.EmailAddress }
            )
            OutlinedTextField(
                state = phone,
                label = { Text(text = stringResource(R.string.keyboard_phone)) },
                inputTransformation = PhoneInputTransformation,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentType = ContentType.PhoneNumber }
            )
            OutlinedSecureTextField(
                state = password,
                label = { Text(text = stringResource(R.string.keyboard_password)) },
                textObfuscationMode = if (passwordVisible) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped,
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(
                            text = stringResource(
                                if (passwordVisible) R.string.keyboard_hide_password else R.string.keyboard_show_password
                            )
                        )
                    }
                },
                isError = showErrors && !passwordValid,
                supportingText = {
                    Text(
                        text = stringResource(
                            if (showErrors && !passwordValid) R.string.keyboard_password_error else R.string.keyboard_password_hint
                        )
                    )
                },
                // The last field submits the form from the keyboard instead of moving the focus.
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                onKeyboardAction = { submit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentType = ContentType.NewPassword }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = hideKeyboard, modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(R.string.keyboard_hide_keyboard))
                }
                Button(onClick = submit, modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(R.string.keyboard_submit))
                }
            }
        }
    }
}

@Composable
private fun KeyboardStatus(visible: Boolean) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if (visible) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = stringResource(if (visible) R.string.keyboard_status_visible else R.string.keyboard_status_hidden),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

private fun errorText(show: Boolean, text: Int): (@Composable () -> Unit)? =
    if (show) {
        { Text(text = stringResource(text)) }
    } else {
        null
    }

@Preview(showBackground = true)
@Composable
private fun KeyboardScreenPreview() {
    AllSetTheme {
        KeyboardScreen(onBack = {})
    }
}
