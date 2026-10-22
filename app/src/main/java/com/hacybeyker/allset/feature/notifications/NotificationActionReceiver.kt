package com.hacybeyker.allset.feature.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/** Handles the "mark as read" action of the message notification without opening the app. */
class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_MARK_AS_READ) DemoNotifications.cancelMessage(context)
    }

    companion object {
        const val ACTION_MARK_AS_READ = "com.hacybeyker.allset.action.MARK_AS_READ"
    }
}
