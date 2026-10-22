package com.hacybeyker.allset.feature.lists.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.components.Avatar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import java.text.Normalizer
import kotlinx.coroutines.launch

private data class Contact(val id: Int, val name: String, val email: String, val favorite: Boolean)

private enum class ContactContentType { Header, Favorites, Contact }

private val contacts: List<Contact> = listOf(
    "Ana Torres", "Andrés Ruiz", "Beatriz Gómez", "Bruno Díaz", "Camila Rojas", "Carlos Vega",
    "Daniela Castro", "Diego Flores", "Elena Paredes", "Emilio Herrera", "Fernanda Silva",
    "Gabriel Núñez", "Gloria Peña", "Hugo Ramos", "Irene Molina", "Javier Ortega", "Julia Campos",
    "Lucía Mendoza", "Luis Navarro", "Marta Ibáñez", "Mateo Cruz", "Natalia León", "Nicolás Soto",
    "Olga Prieto", "Pablo Reyes", "Paula Vargas", "Raúl Guerrero", "Rocío Medina", "Sara Delgado",
    "Sergio Romero", "Tomás Aguilar", "Valeria Ríos", "Víctor Cabrera", "Ximena Fuentes", "Zoe Martín"
).mapIndexed { index, name ->
    val email = Normalizer.normalize(name, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .lowercase()
        .replace(' ', '.')
    Contact(id = index, name = name, email = "$email@ejemplo.com", favorite = index % 6 == 0)
}

private const val ScrollToTopThreshold = 3

@Composable
fun ContactsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val showScrollToTop by remember { derivedStateOf { listState.firstVisibleItemIndex > ScrollToTopThreshold } }
    val sections = remember { contacts.groupBy { it.name.first() } }
    val favorites = remember { contacts.filter { it.favorite } }
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.lists_contacts_title), onBack = onBack) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTop,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                SmallFloatingActionButton(onClick = { scope.launch { listState.animateScrollToItem(0) } }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.lists_scroll_to_top)
                    )
                }
            }
        }
    ) { innerPadding ->
        // The top inset is a modifier so sticky headers stick right below the app bar; the bottom
        // one is content padding so the list keeps scrolling behind the navigation bar.
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                start = innerPadding.calculateStartPadding(layoutDirection),
                end = innerPadding.calculateEndPadding(layoutDirection)
            ),
            contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding() + 80.dp)
        ) {
            item(key = "favorites-header", contentType = ContactContentType.Header) {
                SectionHeader(text = stringResource(R.string.lists_favorites))
            }
            item(key = "favorites", contentType = ContactContentType.Favorites) {
                FavoritesRow(favorites = favorites)
            }
            sections.forEach { (initial, sectionContacts) ->
                stickyHeader(key = "header-$initial", contentType = ContactContentType.Header) {
                    SectionHeader(text = initial.toString())
                }
                items(
                    items = sectionContacts,
                    key = { it.id },
                    contentType = { ContactContentType.Contact }
                ) { contact ->
                    ListItem(
                        headlineContent = { Text(text = contact.name) },
                        supportingContent = { Text(text = contact.email) },
                        leadingContent = { Avatar(name = contact.name) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritesRow(favorites: List<Contact>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = favorites, key = { it.id }) { contact ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Avatar(name = contact.name, size = 56.dp)
                Text(
                    text = contact.name.substringBefore(' '),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactsScreenPreview() {
    AllSetTheme {
        ContactsScreen(onBack = {})
    }
}
