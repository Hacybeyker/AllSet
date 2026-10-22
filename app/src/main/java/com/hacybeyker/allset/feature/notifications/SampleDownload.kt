package com.hacybeyker.allset.feature.notifications

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Simulated download that outlives the screen, so rotating or opening the app from its notification
 * doesn't stop it. A real download would run in WorkManager, which also survives the app process.
 */
object SampleDownload {

    private const val STEP_MILLIS = 400L

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _progress = MutableStateFlow<Int?>(null)

    /** Percentage of the running download, or null when there is none. */
    val progress: StateFlow<Int?> = _progress.asStateFlow()

    fun start(context: Context) {
        if (_progress.value != null) return
        val appContext = context.applicationContext
        scope.launch {
            for (percent in 0..100 step 10) {
                _progress.value = percent
                DemoNotifications.showDownload(appContext, percent)
                if (percent < 100) delay(STEP_MILLIS)
            }
            _progress.value = null
        }
    }
}
