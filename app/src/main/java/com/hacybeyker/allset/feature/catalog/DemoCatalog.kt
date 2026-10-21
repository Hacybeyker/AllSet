package com.hacybeyker.allset.feature.catalog

/**
 * Temporary list of the demos from the XML version of the app (names, descriptions,
 * hierarchy and visibility), kept so they can be rebuilt with Jetpack Compose.
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
            description = "Informacion sobre como realizar bottom sheet"
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
            id = "viewpager",
            name = "ViewPager2",
            description = "Informacion sobre view pager 2",
            children = listOf(
                Demo(
                    id = "viewpager-onboarding",
                    name = "Onboarding",
                    description = "Informacion sobre view pager 2, ejemplo de Onboarding"
                ),
                Demo(
                    id = "viewpager-slider",
                    name = "Slider",
                    description = "Informacion sobre view pager 2, ejemplo de Slider"
                )
            )
        ),
        Demo(
            id = "recyclerview",
            name = "RecyclerView",
            description = "Informacion sobre recycler view",
            children = listOf(
                Demo(
                    id = "recyclerview-viewtype",
                    name = "ViewType",
                    description = "Uso de la recyclerview con viewtype"
                ),
                Demo(
                    id = "recyclerview-recycler-view",
                    name = "Recycler View",
                    description = "Uso de la recyclerview con recycler view"
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
            description = "Informacion sobre el uso de WebView"
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
            id = "tablayout",
            name = "Tab Layout",
            description = "Informacion sobre el uso de tab layout"
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
