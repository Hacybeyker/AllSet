package com.hacybeyker.allset.data

import com.hacybeyker.allset.view.activities.animation.AnimationActivity
import com.hacybeyker.allset.view.activities.animation.normal.AnimationNormalActivityA
import com.hacybeyker.allset.view.activities.animation.swipe.AnimationSwipeActivityA
import com.hacybeyker.allset.view.bottomsheet.BottomSheetActivity
import com.hacybeyker.allset.view.clipboard.ClipBoardActivity
import com.hacybeyker.allset.view.extractcolor.ExtractColorActivity
import com.hacybeyker.allset.view.images.coil.ImagesCoilActivity
import com.hacybeyker.allset.view.keyboard.KeyboardActivity
import com.hacybeyker.allset.view.libraries.LibraryKenBurnsViewActivity
import com.hacybeyker.allset.view.libraries.LibraryTouchImageViewActivity
import com.hacybeyker.allset.view.notifications.NotificationsActivity
import com.hacybeyker.allset.view.preferencessettings.PreferencesSettingsActivity
import com.hacybeyker.allset.view.recycler.RecyclerMainActivity
import com.hacybeyker.allset.view.recycler.viewtype.RecyclerViewTypeActivity
import com.hacybeyker.allset.view.screenshot.ScreenshotActivity
import com.hacybeyker.allset.view.tablayout.TabLayoutMainActivity
import com.hacybeyker.allset.view.taptargetview.TapTargetViewActivity
import com.hacybeyker.allset.view.viewpager.onboarding.ViewPagerOnboardingActivity
import com.hacybeyker.allset.view.viewpager.slider.ViewPagerSliderActivity
import com.hacybeyker.allset.view.webview.WebViewActivity

/**
 * Created by Carlos Osorio on 15/06/2021
 */

class ItemData {
    companion object {
        fun fetchItemData(): ArrayList<Item> {
            return arrayListOf(
                Item(
                    name = "Keyboard",
                    show = false,
                    description = "Informacion sobre como mostrar el keyboard",
                    classActivity = KeyboardActivity::class.java.simpleName
                ),
                Item(
                    name = "Screenshot",
                    show = false,
                    description = "Informacion sobre como realizar screenshots",
                    classActivity = ScreenshotActivity::class.java.simpleName
                ),
                Item(
                    name = "Bottom Sheet",
                    show = true,
                    description = "Informacion sobre como realizar bottom sheet",
                    classActivity = BottomSheetActivity::class.java.simpleName
                ),
                Item(
                    name = "Activity",
                    show = false,
                    description = "Informacion sobre los activities",
                    classActivity = "",
                    child = arrayListOf(
                        Item(
                            name = "Animations",
                            show = true,
                            description = "Muestra distintas formas para animar un activity",
                            classActivity = AnimationActivity::class.java.simpleName,
                            child = arrayListOf(
                                Item(
                                    name = "Animation Normal",
                                    show = true,
                                    description = "Muestra la interaccion izquierda a derecha de un activity con otro, esto es posible mediante anim y con overridePendingTransition",
                                    classActivity = AnimationNormalActivityA::class.java.simpleName
                                ),
                                Item(
                                    name = "Animation Swipe ",
                                    show = true,
                                    description = "Permite realizar swipe de un activity a otro mediante la libreria Slidr",
                                    classActivity = AnimationSwipeActivityA::class.java.simpleName
                                )
                            )
                        )
                    )
                ),
                Item(
                    name = "Fragment",
                    show = false,
                    description = "Informacion sobre los fragmentos",
                    classActivity = ""
                ),
                Item(
                    name = "Biometric",
                    show = false,
                    description = "Informacion sobre biometric",
                    classActivity = ""
                ),
                Item(
                    name = "ViewPager2",
                    show = true,
                    description = "Informacion sobre view pager 2",
                    classActivity = "",
                    child = arrayListOf(
                        Item(
                            name = "Onboarding",
                            show = true,
                            description = "Informacion sobre view pager 2, ejemplo de Onboarding",
                            classActivity = ViewPagerOnboardingActivity::class.java.simpleName
                        ),
                        Item(
                            name = "Slider",
                            show = true,
                            description = "Informacion sobre view pager 2, ejemplo de Slider",
                            classActivity = ViewPagerSliderActivity::class.java.simpleName
                        )
                    )
                ),
                Item(
                    name = "GoogleMaps",
                    show = false,
                    description = "Informacion sobre google maps",
                    classActivity = ""
                ),
                Item(
                    name = "RecyclerView",
                    show = true,
                    description = "Informacion sobre recycler view",
                    classActivity = RecyclerMainActivity::class.java.simpleName,
                    child = arrayListOf(
                        Item(
                            name = "ViewType",
                            show = true,
                            description = "Uso de la recyclerview con viewtype",
                            classActivity = RecyclerViewTypeActivity::class.java.simpleName
                        ),
                        Item(
                            name = "Recycler View",
                            show = true,
                            description = "Uso de la recyclerview con recycler view",
                            classActivity = RecyclerMainActivity::class.java.simpleName
                        )

                    )
                ),
                Item(
                    name = "Images",
                    show = true,
                    description = "Informacion sobre libreria de imagenes",
                    classActivity = "",
                    child = arrayListOf(
                        Item(
                            name = "Coil",
                            show = true,
                            description = "Uso de la libreria de Coil",
                            classActivity = ImagesCoilActivity::class.java.simpleName
                        ),
                        Item(
                            name = "Piccaso",
                            show = false,
                            description = "Uso de la libreria de Piccaso",
                            classActivity = ""
                        )
                    )
                ),
                Item(
                    name = "WebView",
                    show = true,
                    description = "Informacion sobre el uso de WebView",
                    classActivity = WebViewActivity::class.java.simpleName
                ),
                Item(
                    name = "Notifications",
                    show = false,
                    description = "Informacion sobre el uso de notificaciones",
                    classActivity = NotificationsActivity::class.java.simpleName
                ),
                Item(
                    name = "Preferences Settings",
                    show = false,
                    description = "Informacion sobre el uso de preferences settings",
                    classActivity = PreferencesSettingsActivity::class.java.simpleName
                ),
                Item(
                    name = "Libraries",
                    show = true,
                    description = "Informacion sobre librerias ramdom",
                    classActivity = "",
                    child = arrayListOf(
                        Item(
                            name = "KenBurnView",
                            show = true,
                            description = "Informacion sobre KenBurnView",
                            classActivity = LibraryKenBurnsViewActivity::class.java.simpleName
                        ),
                        Item(
                            name = "TouchImageView",
                            show = true,
                            description = "Informacion sobre KenBurnView",
                            classActivity = LibraryTouchImageViewActivity::class.java.simpleName
                        ),
                        Item(
                            name = "Scanner",
                            show = false,
                            description = "Informacion sobre Scanner de barras o QR",
                            classActivity = "",
                            child = arrayListOf()
                        )
                    )
                ),
                Item(
                    name = "Tab Layout",
                    show = true,
                    description = "Informacion sobre el uso de tab layout",
                    classActivity = TabLayoutMainActivity::class.java.simpleName
                ),
                Item(
                    name = "ClipBoardActivity",
                    show = true,
                    description = "Informacion sobre el uso de clip board",
                    classActivity = ClipBoardActivity::class.java.simpleName
                ),
                Item(
                    name = "ExtractColorActivity",
                    show = true,
                    description = "Informacion sobre como extraer colores de una imagen",
                    classActivity = ExtractColorActivity::class.java.simpleName
                ),
                Item(
                    name = "TapTargetView",
                    show = false,
                    description = "Libreria que permite mostrar como tutorial algunos botones o cajas de textos. La libreria usada es taptargetview",
                    classActivity = TapTargetViewActivity::class.java.simpleName
                )
            )
        }
    }
}