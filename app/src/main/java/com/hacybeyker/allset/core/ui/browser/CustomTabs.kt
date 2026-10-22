package com.hacybeyker.allset.core.ui.browser

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

/** Opens [uri] in a Custom Tab of the user's browser; false when the device has no browser. */
fun openInCustomTab(context: Context, uri: Uri): Boolean = try {
    CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()
        .launchUrl(context, uri)
    true
} catch (_: ActivityNotFoundException) {
    false
}
