package com.hacybeyker.allset.view.notifications.vo

import android.app.NotificationManager

/**
 * Created by Carlos Osorio on 28/06/2021
 */

data class NotificationVO(
    val notificationId: String,
    val notificationName: String,
    val notificationImportance: Int
) {

    fun getImportanceText(): String {
        return when (notificationImportance) {
            NotificationManager.IMPORTANCE_NONE -> "NONE"
            NotificationManager.IMPORTANCE_MIN -> "MIN"
            NotificationManager.IMPORTANCE_LOW -> "LOW "
            NotificationManager.IMPORTANCE_DEFAULT -> "DEFAULT"
            NotificationManager.IMPORTANCE_HIGH -> "HIGH"
            else -> ""
        }
    }
}