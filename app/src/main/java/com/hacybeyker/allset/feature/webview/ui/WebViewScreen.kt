package com.hacybeyker.allset.feature.webview.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.hacybeyker.allset.R
import com.hacybeyker.allset.core.ui.components.AllSetTopAppBar
import kotlinx.coroutines.launch

private const val AppOrigin = "https://${WebViewAssetLoader.DEFAULT_DOMAIN}"
private const val StartUrl = "$AppOrigin/assets/web/index.html"
private const val BridgeName = "allset"

@Composable
fun WebViewScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var pageTitle by remember { mutableStateOf("") }
    var progress by remember { mutableIntStateOf(0) }
    var canGoBack by remember { mutableStateOf(false) }
    // Only the last URL survives a configuration change; the page history starts over.
    var currentUrl by rememberSaveable { mutableStateOf(StartUrl) }

    val showMessage: (String) -> Unit = { message ->
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    // A new instance replaces the WebView when its render process dies, since it can't be reused.
    var webViewInstance by remember { mutableIntStateOf(0) }
    val webView = remember(webViewInstance) {
        createDemoWebView(
            context = context,
            replyText = resources.getString(R.string.webview_reply),
            onTitle = { pageTitle = it },
            onProgress = { progress = it },
            onHistoryChanged = { url, back ->
                currentUrl = url
                canGoBack = back
            },
            onExternalLink = { uri ->
                if (!openInCustomTab(context, uri)) showMessage(resources.getString(R.string.webview_no_browser))
            },
            onPageMessage = { text -> showMessage(resources.getString(R.string.webview_message_from_page, text)) },
            onRendererGone = {
                canGoBack = false
                webViewInstance++
                showMessage(resources.getString(R.string.webview_renderer_gone))
            }
        ).apply { loadUrl(currentUrl) }
    }

    // Effects are disposed in reverse order, so destroy() runs last, after onPause().
    DisposableEffect(webView) {
        onDispose { webView.destroy() }
    }
    LifecycleResumeEffect(webView) {
        webView.onResume()
        onPauseOrDispose { webView.onPause() }
    }

    // Like a browser: back goes through the page history before leaving the screen.
    BackHandler(enabled = canGoBack) { webView.goBack() }

    Scaffold(
        modifier = modifier,
        topBar = {
            AllSetTopAppBar(
                title = pageTitle.ifEmpty { stringResource(R.string.webview_title) },
                onBack = onBack,
                actions = {
                    IconButton(onClick = { webView.reload() }) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = stringResource(R.string.webview_reload))
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            key(webView) {
                AndroidView(factory = { webView }, modifier = Modifier.fillMaxSize())
            }
            // Drawn over the page so the content does not jump when loading starts or ends.
            if (progress < 100) {
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
            }
        }
    }
}

/**
 * The page is bundled with the app and served by [WebViewAssetLoader] from an https origin, so no
 * file access or internet permission is needed. Any other host opens outside the app.
 */
@SuppressLint("SetJavaScriptEnabled")
private fun createDemoWebView(
    context: Context,
    replyText: String,
    onTitle: (String) -> Unit,
    onProgress: (Int) -> Unit,
    onHistoryChanged: (url: String, canGoBack: Boolean) -> Unit,
    onExternalLink: (Uri) -> Unit,
    onPageMessage: (String) -> Unit,
    onRendererGone: () -> Unit
): WebView {
    val assetLoader = WebViewAssetLoader.Builder()
        .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
        .build()
    return WebView(context).apply {
        // JavaScript is safe to enable here because the WebView only ever shows the bundled page.
        settings.javaScriptEnabled = true
        settings.allowFileAccess = false
        settings.allowContentAccess = false
        webViewClient = DemoWebViewClient(assetLoader, onHistoryChanged, onExternalLink, onRendererGone)
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                onProgress(newProgress)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                onTitle(title.orEmpty())
            }
        }
        // Unlike addJavascriptInterface, the listener only accepts messages from the app's own origin.
        if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
            WebViewCompat.addWebMessageListener(this, BridgeName, setOf(AppOrigin)) { _, message, _, isMainFrame, replyProxy ->
                val text = message.data
                if (isMainFrame && text != null) {
                    onPageMessage(text)
                    replyProxy.postMessage(replyText)
                }
            }
        }
    }
}

// onRenderProcessGone is implemented below; androidx.webkit's lint check also flags the
// WebViewClient() superclass call in the Kotlin class header, which is a false positive.
@SuppressLint("MissingOnRenderProcessGone")
private class DemoWebViewClient(
    private val assetLoader: WebViewAssetLoader,
    private val onHistoryChanged: (url: String, canGoBack: Boolean) -> Unit,
    private val onExternalLink: (Uri) -> Unit,
    private val onRendererGone: () -> Unit
) : WebViewClient() {

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest): WebResourceResponse? =
        assetLoader.shouldInterceptRequest(request.url)

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
        if (request.url.host == WebViewAssetLoader.DEFAULT_DOMAIN) return false
        onExternalLink(request.url)
        return true
    }

    override fun doUpdateVisitedHistory(view: WebView, url: String?, isReload: Boolean) {
        onHistoryChanged(url ?: StartUrl, view.canGoBack())
    }

    // Returning true keeps the app alive when the renderer crashes or is killed for memory.
    override fun onRenderProcessGone(view: WebView, detail: RenderProcessGoneDetail): Boolean {
        onRendererGone()
        return true
    }
}

private fun openInCustomTab(context: Context, uri: Uri): Boolean = try {
    CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()
        .launchUrl(context, uri)
    true
} catch (_: ActivityNotFoundException) {
    false
}
