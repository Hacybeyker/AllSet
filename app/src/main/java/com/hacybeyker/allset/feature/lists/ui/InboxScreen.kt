package com.hacybeyker.allset.feature.lists.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class Message(
    val id: Int,
    val sender: String,
    val subject: String,
    val preview: String,
    val time: String,
    val read: Boolean = false
)

private val sampleMessages = listOf(
    Message(1, "Lucía Mendoza", "Reunión del lunes", "Te comparto la agenda para revisarla antes de la reunión.", "9:41"),
    Message(2, "Diego Flores", "Fotos del viaje", "Ya subí las fotos al álbum compartido, dime cuáles te gustan.", "8:15"),
    Message(3, "Equipo de soporte", "Tu solicitud fue atendida", "Revisamos tu caso y ya está resuelto. Gracias por escribirnos.", "Ayer", read = true),
    Message(4, "Camila Rojas", "Propuesta de diseño", "Adjunto la nueva versión con los cambios que conversamos.", "Ayer"),
    Message(5, "Boletín semanal", "Novedades de Jetpack Compose", "Las mejores prácticas de la semana para tus listas y animaciones.", "Lun", read = true),
    Message(6, "Pablo Reyes", "¿Almorzamos el jueves?", "Conozco un lugar nuevo cerca de la oficina.", "Lun"),
    Message(7, "Valeria Ríos", "Factura de octubre", "Te envío la factura del mes para tu registro.", "Dom", read = true),
    Message(8, "Javier Ortega", "Revisión del código", "Dejé algunos comentarios en tu pull request.", "Sáb"),
    Message(9, "Natalia León", "Entradas para el concierto", "Conseguí dos entradas, ¿te animas?", "Vie", read = true),
    Message(10, "Sergio Romero", "Plan del proyecto", "Actualicé el cronograma con las nuevas fechas de entrega.", "Jue"),
    Message(11, "Elena Paredes", "Cumpleaños de Ana", "Estamos organizando una sorpresa para el sábado.", "Mié", read = true),
    Message(12, "Hugo Ramos", "Documentos pendientes", "Faltan dos firmas para cerrar el trámite.", "Mar")
)

private val messagesSaver = listSaver<SnapshotStateList<Message>, Int>(
    save = { list -> list.flatMap { listOf(it.id, if (it.read) 1 else 0) } },
    restore = { saved ->
        saved.chunked(2).mapNotNull { (id, read) ->
            sampleMessages.find { it.id == id }?.copy(read = read == 1)
        }.toMutableStateList()
    }
)

private const val RefreshDelayMillis = 1_500L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current
    val snackbarHostState = remember { SnackbarHostState() }
    val messages = rememberSaveable(saver = messagesSaver) { sampleMessages.toMutableStateList() }
    var isRefreshing by remember { mutableStateOf(false) }

    val toggleRead: (Message) -> Unit = { message ->
        val index = messages.indexOfFirst { it.id == message.id }
        if (index >= 0) messages[index] = message.copy(read = !message.read)
    }
    val delete: (Message) -> Unit = { message ->
        val index = messages.indexOfFirst { it.id == message.id }
        if (index >= 0) {
            messages.removeAt(index)
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                val result = snackbarHostState.showSnackbar(
                    message = resources.getString(R.string.lists_inbox_deleted),
                    actionLabel = resources.getString(R.string.lists_inbox_undo)
                )
                if (result == SnackbarResult.ActionPerformed) {
                    messages.add(index.coerceAtMost(messages.size), message)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.lists_inbox_title), onBack = onBack) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    delay(RefreshDelayMillis)
                    messages.clear()
                    messages.addAll(sampleMessages)
                    isRefreshing = false
                }
            },
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())
            ) {
                item(key = "hint", contentType = "hint") {
                    Text(
                        text = stringResource(R.string.lists_inbox_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                if (messages.isEmpty()) {
                    item(key = "empty", contentType = "empty") {
                        EmptyInbox(modifier = Modifier.fillParentMaxSize())
                    }
                }
                items(items = messages, key = { it.id }, contentType = { "message" }) { message ->
                    SwipeableMessage(
                        message = message,
                        onToggleRead = { toggleRead(message) },
                        onDelete = { delete(message) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@Composable
private fun SwipeableMessage(
    message: Message,
    onToggleRead: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    // Not saveable on purpose: a message restored with "Deshacer" must come back in place,
    // not with the swiped-away position saved for its key.
    val positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
    val dismissState = remember { SwipeToDismissBoxState(SwipeToDismissBoxValue.Settled, positionalThreshold) }
    val currentOnToggleRead by rememberUpdatedState(onToggleRead)
    val currentOnDelete by rememberUpdatedState(onDelete)
    // SwipeToDismissBox restarts its dismiss effect whenever this lambda changes, so it must be stable.
    val onDismiss: (SwipeToDismissBoxValue) -> Unit = remember(dismissState, scope) {
        { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    currentOnToggleRead()
                    scope.launch { dismissState.reset() }
                }
                SwipeToDismissBoxValue.EndToStart -> currentOnDelete()
                SwipeToDismissBoxValue.Settled -> Unit
            }
        }
    }
    val toggleReadLabel = stringResource(
        if (message.read) R.string.lists_inbox_mark_unread else R.string.lists_inbox_mark_read
    )
    val deleteLabel = stringResource(R.string.lists_inbox_delete)

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val (color, icon, alignment) = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Triple(
                    MaterialTheme.colorScheme.primaryContainer,
                    if (message.read) Icons.Filled.Email else Icons.Filled.Done,
                    Alignment.CenterStart
                )
                SwipeToDismissBoxValue.EndToStart -> Triple(
                    MaterialTheme.colorScheme.errorContainer,
                    Icons.Filled.Delete,
                    Alignment.CenterEnd
                )
                SwipeToDismissBoxValue.Settled -> Triple(Color.Transparent, null, Alignment.Center)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 24.dp),
                contentAlignment = alignment
            ) {
                if (icon != null) Icon(imageVector = icon, contentDescription = null)
            }
        },
        // Swipe gestures are not available to TalkBack users, so the same actions are exposed here.
        modifier = modifier.semantics {
            customActions = listOf(
                CustomAccessibilityAction(toggleReadLabel) { onToggleRead(); true },
                CustomAccessibilityAction(deleteLabel) { onDelete(); true }
            )
        },
        onDismiss = onDismiss
    ) {
        MessageRow(message = message)
    }
}

@Composable
private fun MessageRow(message: Message) {
    val fontWeight = if (message.read) FontWeight.Normal else FontWeight.Bold
    ListItem(
        headlineContent = { Text(text = message.sender, fontWeight = fontWeight) },
        supportingContent = {
            Column {
                Text(
                    text = message.subject,
                    fontWeight = fontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = message.preview, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        },
        leadingContent = { Avatar(name = message.sender) },
        trailingContent = { Text(text = message.time, style = MaterialTheme.typography.labelMedium) }
    )
}

@Composable
private fun EmptyInbox(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Email,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(48.dp)
        )
        Text(text = stringResource(R.string.lists_inbox_empty_title), style = MaterialTheme.typography.titleMedium)
        Text(
            text = stringResource(R.string.lists_inbox_empty_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InboxScreenPreview() {
    AllSetTheme {
        InboxScreen(onBack = {})
    }
}
