package com.hacybeyker.allset.feature.catalog

import com.hacybeyker.allset.navigation.BottomSheetDemo
import com.hacybeyker.allset.navigation.CarouselDemo
import com.hacybeyker.allset.navigation.ContactsDemo
import com.hacybeyker.allset.navigation.InboxDemo
import com.hacybeyker.allset.navigation.OnboardingDemo
import com.hacybeyker.allset.navigation.ShelvesDemo
import com.hacybeyker.allset.navigation.TabsDemo
import com.hacybeyker.allset.navigation.WebViewDemo

/**
 * Demos of the app. Entries without a destination are pending and keep their original name,
 * description, hierarchy and visibility until they are rebuilt with Jetpack Compose.
 */
object DemoCatalog {

    val demos: List<Demo> = listOf(
        Demo(
            id = "keyboard",
            name = "Keyboard",
            description = "Informacion sobre como mostrar el keyboard",
            show = false
        ),
        Demo(
            id = "bottom-sheet",
            name = "Bottom Sheet",
            description = "Hojas inferiores modales y persistentes con Material 3",
            destination = BottomSheetDemo
        ),
        Demo(
            id = "activity",
            name = "Activity",
            description = "Informacion sobre los activities",
            show = false,
            children = listOf(
                Demo(
                    id = "activity-animations",
                    name = "Animations",
                    description = "Muestra distintas formas para animar un activity",
                    children = listOf(
                        Demo(
                            id = "activity-animation-normal",
                            name = "Animation Normal",
                            description = "Muestra la interaccion izquierda a derecha de un activity con otro, esto es posible mediante anim y con overridePendingTransition"
                        ),
                        Demo(
                            id = "activity-animation-swipe",
                            name = "Animation Swipe",
                            description = "Permite realizar swipe de un activity a otro mediante la libreria Slidr"
                        )
                    )
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
                ),
                Demo(
                    id = "images-picasso",
                    name = "Piccaso",
                    description = "Uso de la libreria de Piccaso",
                    show = false
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
            name = "Notifications",
            description = "Informacion sobre el uso de notificaciones",
            show = false
        ),
        Demo(
            id = "preferences",
            name = "Preferences Settings",
            description = "Informacion sobre el uso de preferences settings",
            show = false
        ),
        Demo(
            id = "libraries",
            name = "Libraries",
            description = "Informacion sobre librerias ramdom",
            children = listOf(
                Demo(
                    id = "libraries-kenburns",
                    name = "KenBurnView",
                    description = "Informacion sobre KenBurnView"
                ),
                Demo(
                    id = "libraries-touchimageview",
                    name = "TouchImageView",
                    description = "Informacion sobre KenBurnView"
                ),
                Demo(
                    id = "libraries-scanner",
                    name = "Scanner",
                    description = "Informacion sobre Scanner de barras o QR",
                    show = false
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
            name = "ClipBoardActivity",
            description = "Informacion sobre el uso de clip board",
            show = false
        ),
        Demo(
            id = "taptargetview",
            name = "TapTargetView",
            description = "Libreria que permite mostrar como tutorial algunos botones o cajas de textos. La libreria usada es taptargetview",
            show = false
        )
    )

    fun find(id: String): Demo = flatten(demos).first { it.id == id }

    private fun flatten(demos: List<Demo>): List<Demo> = demos.flatMap { listOf(it) + flatten(it.children) }
}
