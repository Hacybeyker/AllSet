package com.hacybeyker.allset.view.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat

/**
 * Created by Carlos Osorio on 30/06/2021
 */

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Notificacion Clicked", Toast.LENGTH_SHORT).show()
        val nm = NotificationManagerCompat.from(context)
        nm.cancel(1)
    }

}