package com.hacybeyker.allset.feature.catalog

import com.hacybeyker.allset.navigation.BottomSheetDemo
import com.hacybeyker.allset.navigation.CarouselDemo
import com.hacybeyker.allset.navigation.ClipboardDemo
import com.hacybeyker.allset.navigation.ContactsDemo
import com.hacybeyker.allset.navigation.ContentAnimationsDemo
import com.hacybeyker.allset.navigation.InboxDemo
import com.hacybeyker.allset.navigation.KenBurnsDemo
import com.hacybeyker.allset.navigation.KeyboardDemo
import com.hacybeyker.allset.navigation.NotificationsDemo
import com.hacybeyker.allset.navigation.OnboardingDemo
import com.hacybeyker.allset.navigation.SettingsDemo
import com.hacybeyker.allset.navigation.SharedElementsDemo
import com.hacybeyker.allset.navigation.ShelvesDemo
import com.hacybeyker.allset.navigation.SpotlightDemo
import com.hacybeyker.allset.navigation.TabsDemo
import com.hacybeyker.allset.navigation.VerticalTransitionDemo
import com.hacybeyker.allset.navigation.WebViewDemo
import com.hacybeyker.allset.navigation.ZoomDemo

/**
 * Demos of the app. Entries without a destination are pending and keep their original name,
 * description, hierarchy and visibility until they are rebuilt with Jetpack Compose.
 */
object DemoCatalog {

    val demos: List<Demo> = listOf(
        Demo(
            id = "keyboard",
            name = "Teclado y formularios",
            description = "Teclado según el dato, acción para pasar al siguiente campo, autocompletado y campos que el teclado no tapa",
            destination = KeyboardDemo
        ),
        Demo(
            id = "bottom-sheet",
            name = "Bottom Sheet",
            description = "Hojas inferiores modales y persistentes con Material 3",
            destination = BottomSheetDemo
        ),
        Demo(
            id = "animations",
            name = "Animaciones",
            description = "Elementos compartidos, animaciones de contenido y transiciones entre pantallas",
            children = listOf(
                Demo(
                    id = "animations-shared",
                    name = "Elementos compartidos",
                    description = "La tarjeta y su título se transforman en el detalle con SharedTransitionLayout",
                    destination = SharedElementsDemo
                ),
                Demo(
                    id = "animations-content",
                    name = "Animaciones de contenido",
                    description = "AnimatedContent, AnimatedVisibility, animateContentSize, valores animados y animación infinita",
                    destination = ContentAnimationsDemo
                ),
                Demo(
                    id = "animations-transition",
                    name = "Transición entre pantallas",
                    description = "Una pantalla que entra y sale desde abajo, también con el gesto atrás predictivo",
                    destination = VerticalTransitionDemo
                )
            )
        ),
        Demo(
            id = "pager",
            name = "Pager",
            description = "HorizontalPager y carruseles de Material 3",
            children = listOf(
                Demo(
                    id = "pager-onboarding",
                    name = "Onboarding",
                    description = "Introducción con HorizontalPager, indicador y transición entre páginas",
                    destination = OnboardingDemo
                ),
                Demo(
                    id = "pager-carousel",
                    name = "Carousel",
                    description = "Banner automático y carruseles multi-browse, hero y sin contención",
                    destination = CarouselDemo
                )
            )
        ),
        Demo(
            id = "lists",
            name = "Listas",
            description = "LazyColumn y LazyRow: secciones, gestos y filas horizontales",
            children = listOf(
                Demo(
                    id = "lists-contacts",
                    name = "Contactos por sección",
                    description = "Encabezados fijos, varios tipos de elemento y botón para volver arriba",
                    destination = ContactsDemo
                ),
                Demo(
                    id = "lists-inbox",
                    name = "Bandeja con gestos",
                    description = "Deslizar para marcar como leído o eliminar, deshacer y arrastrar para recargar",
                    destination = InboxDemo
                ),
                Demo(
                    id = "lists-shelves",
                    name = "Filas horizontales",
                    description = "LazyRow dentro de LazyColumn que se ajusta a cada tarjeta",
                    destination = ShelvesDemo
                )
            )
        ),
        Demo(
            id = "images",
            name = "Images",
            description = "Informacion sobre libreria de imagenes",
            show = false,
            children = listOf(
                Demo(
                    id = "images-coil",
                    name = "Coil",
                    description = "Uso de la libreria de Coil"
                )
            )
        ),
        Demo(
            id = "webview",
            name = "WebView",
            description = "Contenido web local con historial, enlaces en Custom Tabs y mensajes con la página",
            destination = WebViewDemo
        ),
        Demo(
            id = "notifications",
            name = "Notificaciones",
            description = "Permiso, canales, estilos, acciones y abrir la app desde una notificación",
            destination = NotificationsDemo
        ),
        Demo(
            id = "settings",
            name = "Ajustes",
            description = "Tema y color dinámico guardados con DataStore y aplicados a toda la app",
            destination = SettingsDemo
        ),
        Demo(
            id = "image-effects",
            name = "Efectos de imagen",
            description = "Componentes propios en Compose: efecto Ken Burns y zoom con gestos",
            children = listOf(
                Demo(
                    id = "image-effects-ken-burns",
                    name = "Ken Burns",
                    description = "Zoom y desplazamiento lentos y aleatorios sobre cualquier contenido",
                    destination = KenBurnsDemo
                ),
                Demo(
                    id = "image-effects-zoom",
                    name = "Zoom con gestos",
                    description = "Pellizcar para acercar, arrastrar y tocar dos veces para alternar el zoom",
                    destination = ZoomDemo
                )
            )
        ),
        Demo(
            id = "tabs",
            name = "Tabs",
            description = "PrimaryTabRow con HorizontalPager, pestañas desplazables y badges",
            destination = TabsDemo
        ),
        Demo(
            id = "clipboard",
            name = "Portapapeles",
            description = "Copiar y pegar con LocalClipboard, contenido sensible y lectura solo al pegar",
            destination = ClipboardDemo
        ),
        Demo(
            id = "spotlight",
            name = "Tutorial con foco",
            description = "Componente propio que oscurece la pantalla y resalta elementos paso a paso",
            destination = SpotlightDemo
        )
    )

    fun find(id: String): Demo = flatten(demos).first { it.id == id }

    private fun flatten(demos: List<Demo>): List<Demo> = demos.flatMap { listOf(it) + flatten(it.children) }
}
