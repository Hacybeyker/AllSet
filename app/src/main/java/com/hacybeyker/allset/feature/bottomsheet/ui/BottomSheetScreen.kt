package com.hacybeyker.allset.feature.bottomsheet.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import kotlinx.coroutines.launch

private enum class ModalSheet { Actions, Comment }

private enum class SheetAction(@StringRes val label: Int, val icon: ImageVector) {
    Share(R.string.bottom_sheet_action_share, Icons.Filled.Share),
    Favorite(R.string.bottom_sheet_action_favorite, Icons.Filled.FavoriteBorder),
    Edit(R.string.bottom_sheet_action_edit, Icons.Filled.Edit),
    Delete(R.string.bottom_sheet_action_delete, Icons.Filled.Delete)
}

// Height of BottomSheetDefaults.DragHandle: 4.dp handle plus 22.dp vertical padding on each side.
private val DragHandleHeight = 48.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current
    val scaffoldState = rememberBottomSheetScaffoldState()
    var openSheet by rememberSaveable { mutableStateOf<ModalSheet?>(null) }
    var dismissOnClickOutside by rememberSaveable { mutableStateOf(true) }
    var dismissOnBackPress by rememberSaveable { mutableStateOf(true) }
    var draggable by rememberSaveable { mutableStateOf(true) }
    val showMessage: (String) -> Unit = { message ->
        scope.launch { scaffoldState.snackbarHostState.showSnackbar(message) }
    }
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    var routeSummaryHeight by remember { mutableStateOf(0.dp) }
    val routeExpanded = scaffoldState.bottomSheetState.targetValue == SheetValue.Expanded

    // Like Maps: with the persistent sheet expanded, back collapses it before leaving the screen.
    BackHandler(enabled = routeExpanded) {
        scope.launch { scaffoldState.bottomSheetState.partialExpand() }
    }

    BottomSheetScaffold(
        sheetContent = {
            RouteSheetContent(
                expanded = routeExpanded,
                onToggle = {
                    scope.launch {
                        if (routeExpanded) {
                            scaffoldState.bottomSheetState.partialExpand()
                        } else {
                            scaffoldState.bottomSheetState.expand()
                        }
                    }
                },
                navigationBarHeight = navigationBarHeight,
                onSummaryHeightChange = { routeSummaryHeight = it }
            )
        },
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetPeekHeight = DragHandleHeight + routeSummaryHeight,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.bottom_sheet_title), onBack = onBack) },
        snackbarHost = { SnackbarHost(hostState = scaffoldState.snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.bottom_sheet_intro),
                style = MaterialTheme.typography.bodyMedium
            )
            SectionTitle(text = stringResource(R.string.bottom_sheet_modal_title))
            Button(onClick = { openSheet = ModalSheet.Actions }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.bottom_sheet_open_actions))
            }
            SwitchRow(
                text = stringResource(R.string.bottom_sheet_dismiss_outside),
                checked = dismissOnClickOutside,
                onCheckedChange = { dismissOnClickOutside = it }
            )
            SwitchRow(
                text = stringResource(R.string.bottom_sheet_dismiss_back),
                checked = dismissOnBackPress,
                onCheckedChange = { dismissOnBackPress = it }
            )
            SwitchRow(
                text = stringResource(R.string.bottom_sheet_draggable),
                checked = draggable,
                onCheckedChange = { draggable = it }
            )
            Button(onClick = { openSheet = ModalSheet.Comment }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.bottom_sheet_open_comment))
            }
            SectionTitle(text = stringResource(R.string.bottom_sheet_persistent_title))
            Text(
                text = stringResource(R.string.bottom_sheet_persistent_hint),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    when (openSheet) {
        ModalSheet.Actions -> ActionsBottomSheet(
            sheetGesturesEnabled = draggable,
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = dismissOnBackPress,
                shouldDismissOnClickOutside = dismissOnClickOutside
            ),
            onActionSelected = { action ->
                openSheet = null
                showMessage(
                    resources.getString(R.string.bottom_sheet_action_selected, resources.getString(action.label))
                )
            },
            onDismissRequest = { openSheet = null }
        )

        ModalSheet.Comment -> CommentBottomSheet(
            onSend = {
                openSheet = null
                showMessage(resources.getString(R.string.bottom_sheet_comment_sent))
            },
            onDismissRequest = { openSheet = null }
        )

        null -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionsBottomSheet(
    sheetGesturesEnabled: Boolean,
    properties: ModalBottomSheetProperties,
    onActionSelected: (SheetAction) -> Unit,
    onDismissRequest: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        sheetGesturesEnabled = sheetGesturesEnabled,
        properties = properties
    ) {
        SheetAction.entries.forEach { action ->
            val color = if (action == SheetAction.Delete) MaterialTheme.colorScheme.error else LocalContentColor.current
            ListItem(
                headlineContent = { Text(text = stringResource(action.label), color = color) },
                leadingContent = { Icon(imageVector = action.icon, contentDescription = null, tint = color) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier.clickable {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) onActionSelected(action)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommentBottomSheet(onSend: () -> Unit, onDismissRequest: () -> Unit) {
    val scope = rememberCoroutineScope()
    var comment by rememberSaveable { mutableStateOf("") }
    var closing by remember { mutableStateOf(false) }
    var showDiscardDialog by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { value ->
            val blocked = value == SheetValue.Hidden && comment.isNotBlank() && !closing
            if (blocked) showDiscardDialog = true
            !blocked
        }
    )
    val close: (onClosed: () -> Unit) -> Unit = { onClosed ->
        closing = true
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) onClosed()
        }
    }

    ModalBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState) {
        // The sheet's back handling ignores confirmValueChange, so unsent text is guarded here.
        BackHandler(enabled = comment.isNotBlank() && !closing) { showDiscardDialog = true }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.bottom_sheet_comment_title),
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text(text = stringResource(R.string.bottom_sheet_comment_label)) },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { close(onSend) },
                enabled = comment.isNotBlank(),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = stringResource(R.string.bottom_sheet_comment_send))
            }
        }
    }

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text(text = stringResource(R.string.bottom_sheet_discard_title)) },
            text = { Text(text = stringResource(R.string.bottom_sheet_discard_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDiscardDialog = false
                        close(onDismissRequest)
                    }
                ) {
                    Text(text = stringResource(R.string.bottom_sheet_discard_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text(text = stringResource(R.string.bottom_sheet_discard_cancel))
                }
            }
        )
    }
}

/**
 * The summary is measured so the collapsed sheet shows exactly the summary, whatever the font
 * size. Its bottom spacer covers the navigation bar, so no step is drawn behind it.
 */
@Composable
private fun RouteSheetContent(
    expanded: Boolean,
    onToggle: () -> Unit,
    navigationBarHeight: Dp,
    onSummaryHeightChange: (Dp) -> Unit
) {
    val density = LocalDensity.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.onSizeChanged { size ->
                onSummaryHeightChange(with(density) { size.height.toDp() })
            }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.bottom_sheet_route_summary),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = stringResource(R.string.bottom_sheet_route_detail),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                TextButton(onClick = onToggle) {
                    Text(
                        text = stringResource(
                            if (expanded) R.string.bottom_sheet_route_hide_steps else R.string.bottom_sheet_route_show_steps
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp + navigationBarHeight))
        }
        stringArrayResource(R.array.bottom_sheet_route_steps).forEachIndexed { index, step ->
            ListItem(
                headlineContent = { Text(text = step) },
                leadingContent = { Text(text = "${index + 1}", style = MaterialTheme.typography.titleMedium) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }
        Spacer(modifier = Modifier.height(16.dp + navigationBarHeight))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun SwitchRow(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = checked, role = Role.Switch, onValueChange = onCheckedChange)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = null)
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetScreenPreview() {
    AllSetTheme {
        BottomSheetScreen(onBack = {})
    }
}
