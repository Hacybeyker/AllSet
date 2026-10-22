package com.hacybeyker.allset.feature.tabs.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import com.hacybeyker.allset.core.ui.components.Avatar
import com.hacybeyker.allset.core.ui.theme.AllSetTheme
import kotlinx.coroutines.launch

private enum class TabsPage(@StringRes val label: Int, val icon: ImageVector) {
    Explore(R.string.tabs_explore, Icons.Filled.Search),
    Messages(R.string.tabs_messages, Icons.Filled.Email),
    Profile(R.string.tabs_profile, Icons.Filled.Person)
}

private enum class PlaceCategory(@StringRes val label: Int) {
    All(R.string.tabs_category_all),
    Beaches(R.string.tabs_category_beaches),
    Mountains(R.string.tabs_category_mountains),
    Cities(R.string.tabs_category_cities),
    Forests(R.string.tabs_category_forests),
    Deserts(R.string.tabs_category_deserts),
    Lakes(R.string.tabs_category_lakes)
}

private data class Place(val name: String, val country: String, val category: PlaceCategory)

private data class Conversation(val name: String, val lastMessage: String, val time: String, val unread: Boolean)

private val places = listOf(
    Place("Máncora", "Perú", PlaceCategory.Beaches),
    Place("Punta Cana", "República Dominicana", PlaceCategory.Beaches),
    Place("Cancún", "México", PlaceCategory.Beaches),
    Place("Huascarán", "Perú", PlaceCategory.Mountains),
    Place("Aconcagua", "Argentina", PlaceCategory.Mountains),
    Place("Torres del Paine", "Chile", PlaceCategory.Mountains),
    Place("Lima", "Perú", PlaceCategory.Cities),
    Place("Bogotá", "Colombia", PlaceCategory.Cities),
    Place("Madrid", "España", PlaceCategory.Cities),
    Place("Amazonía", "Brasil", PlaceCategory.Forests),
    Place("Monteverde", "Costa Rica", PlaceCategory.Forests),
    Place("Atacama", "Chile", PlaceCategory.Deserts),
    Place("Huacachina", "Perú", PlaceCategory.Deserts),
    Place("Titicaca", "Perú y Bolivia", PlaceCategory.Lakes),
    Place("Atitlán", "Guatemala", PlaceCategory.Lakes)
)

private val conversations = listOf(
    Conversation("Lucía Mendoza", "¿Te parece si lo vemos mañana temprano?", "10:12", unread = true),
    Conversation("Diego Flores", "Te mandé las fotos del viaje.", "9:30", unread = true),
    Conversation("Camila Rojas", "¡Listo! Ya subí la nueva versión.", "Ayer", unread = true),
    Conversation("Pablo Reyes", "Nos vemos el jueves en el almuerzo.", "Ayer", unread = false),
    Conversation("Valeria Ríos", "Gracias por la ayuda con la factura.", "Lun", unread = false),
    Conversation("Javier Ortega", "Revisé tu código, se ve muy bien.", "Dom", unread = false),
    Conversation("Natalia León", "¿Vas al concierto?", "Sáb", unread = false)
)

@Composable
fun TabsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val pages = TabsPage.entries
    val pagerState = rememberPagerState(pageCount = { pages.size })
    // The badge clears when the tab is opened; the rows stay bold until the user leaves the tab.
    var badgeCleared by rememberSaveable { mutableStateOf(false) }
    var conversationsRead by rememberSaveable { mutableStateOf(false) }
    val unreadCount = if (badgeCleared) 0 else conversations.count { it.unread }

    LaunchedEffect(pagerState.settledPage) {
        if (pages[pagerState.settledPage] == TabsPage.Messages) {
            badgeCleared = true
        } else if (badgeCleared) {
            conversationsRead = true
        }
    }

    // Like many apps with tabs: back returns to the first tab before leaving the screen.
    BackHandler(enabled = pagerState.currentPage != 0) {
        scope.launch { pagerState.animateScrollToPage(0) }
    }

    Scaffold(
        modifier = modifier,
        topBar = { AllSetTopAppBar(title = stringResource(R.string.tabs_title), onBack = onBack) }
    ) { innerPadding ->
        val bottomPadding = innerPadding.calculateBottomPadding()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
                pages.forEachIndexed { index, page ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(text = stringResource(page.label)) },
                        icon = { TabIcon(page = page, unreadCount = unreadCount) }
                    )
                }
            }
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { index ->
                when (pages[index]) {
                    TabsPage.Explore -> ExplorePage(bottomPadding = bottomPadding)
                    TabsPage.Messages -> MessagesPage(read = conversationsRead, bottomPadding = bottomPadding)
                    TabsPage.Profile -> ProfilePage(bottomPadding = bottomPadding)
                }
            }
        }
    }
}

@Composable
private fun TabIcon(page: TabsPage, unreadCount: Int) {
    if (page == TabsPage.Messages && unreadCount > 0) {
        val description = pluralStringResource(R.plurals.tabs_unread_messages, unreadCount, unreadCount)
        BadgedBox(
            badge = {
                Badge(modifier = Modifier.semantics { contentDescription = description }) {
                    Text(text = unreadCount.toString())
                }
            }
        ) {
            Icon(imageVector = page.icon, contentDescription = null)
        }
    } else {
        Icon(imageVector = page.icon, contentDescription = null)
    }
}

/** Secondary tabs inside the page filter its content; the selection survives switching tabs. */
@Composable
private fun ExplorePage(bottomPadding: Dp) {
    var selectedCategory by rememberSaveable { mutableStateOf(PlaceCategory.All) }
    Column(modifier = Modifier.fillMaxSize()) {
        SecondaryScrollableTabRow(selectedTabIndex = selectedCategory.ordinal, edgePadding = 16.dp) {
            PlaceCategory.entries.forEach { category ->
                Tab(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    text = { Text(text = stringResource(category.label)) }
                )
            }
        }
        AnimatedContent(targetState = selectedCategory, label = "category", modifier = Modifier.weight(1f)) { category ->
            val categoryPlaces = remember(category) {
                if (category == PlaceCategory.All) places else places.filter { it.category == category }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = bottomPadding)
            ) {
                items(items = categoryPlaces, key = { it.name }) { place ->
                    ListItem(
                        headlineContent = { Text(text = place.name) },
                        supportingContent = { Text(text = place.country) },
                        leadingContent = { Avatar(name = place.name) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MessagesPage(read: Boolean, bottomPadding: Dp) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = bottomPadding)
    ) {
        items(items = conversations, key = { it.name }) { conversation ->
            val fontWeight = if (conversation.unread && !read) FontWeight.Bold else FontWeight.Normal
            ListItem(
                headlineContent = { Text(text = conversation.name, fontWeight = fontWeight) },
                supportingContent = {
                    Text(
                        text = conversation.lastMessage,
                        fontWeight = fontWeight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingContent = { Avatar(name = conversation.name) },
                trailingContent = { Text(text = conversation.time, style = MaterialTheme.typography.labelMedium) }
            )
        }
    }
}

@Composable
private fun ProfilePage(bottomPadding: Dp) {
    var notifications by rememberSaveable { mutableStateOf(true) }
    var compactMode by rememberSaveable { mutableStateOf(false) }
    var wifiOnly by rememberSaveable { mutableStateOf(true) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = bottomPadding)
    ) {
        item(key = "header") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Avatar(name = ProfileName, size = 96.dp)
                Text(
                    text = ProfileName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = ProfileEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item(key = "notifications") {
            SwitchListItem(
                text = stringResource(R.string.tabs_profile_notifications),
                checked = notifications,
                onCheckedChange = { notifications = it }
            )
        }
        item(key = "compact") {
            SwitchListItem(
                text = stringResource(R.string.tabs_profile_compact),
                checked = compactMode,
                onCheckedChange = { compactMode = it }
            )
        }
        item(key = "wifi") {
            SwitchListItem(
                text = stringResource(R.string.tabs_profile_wifi),
                checked = wifiOnly,
                onCheckedChange = { wifiOnly = it }
            )
        }
    }
}

private const val ProfileName = "Ana Torres"
private const val ProfileEmail = "ana.torres@ejemplo.com"

@Composable
private fun SwitchListItem(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    ListItem(
        headlineContent = { Text(text = text) },
        trailingContent = { Switch(checked = checked, onCheckedChange = null) },
        modifier = Modifier.toggleable(value = checked, role = Role.Switch, onValueChange = onCheckedChange)
    )
}

@Preview(showBackground = true)
@Composable
private fun TabsScreenPreview() {
    AllSetTheme {
        TabsScreen(onBack = {})
    }
}
