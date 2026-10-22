package com.hacybeyker.allset.feature.notifications

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.IconCompat
import com.hacybeyker.allset.MainActivity
import com.hacybeyker.allset.R
import kotlin.math.roundToInt

/** Channels and notifications of the notifications demo. */
object DemoNotifications {

    /** Opens the notifications demo when MainActivity is started from a notification. */
    const val EXTRA_OPEN_NOTIFICATIONS_DEMO = "com.hacybeyker.allset.extra.OPEN_NOTIFICATIONS_DEMO"

    private const val MESSAGE_ID = 1
    private const val PICTURE_ID = 2
    private const val DOWNLOAD_ID = 3

    private const val CHANNEL_MESSAGES = "messages"
    private const val CHANNEL_PROMOTIONS = "promotions"
    private const val CHANNEL_DOWNLOADS = "downloads"

    private const val MONOGRAM = "A"

    /** Safe to call every time: existing channels keep the settings the user chose. */
    fun createChannels(context: Context) {
        val channels = listOf(
            channel(context, CHANNEL_MESSAGES, NotificationManagerCompat.IMPORTANCE_HIGH, R.string.notifications_channel_messages, R.string.notifications_channel_messages_description),
            channel(context, CHANNEL_PROMOTIONS, NotificationManagerCompat.IMPORTANCE_DEFAULT, R.string.notifications_channel_promotions, R.string.notifications_channel_promotions_description),
            channel(context, CHANNEL_DOWNLOADS, NotificationManagerCompat.IMPORTANCE_LOW, R.string.notifications_channel_downloads, R.string.notifications_channel_downloads_description)
        )
        NotificationManagerCompat.from(context).createNotificationChannelsCompat(channels)
    }

    /** Covers notifications turned off in Settings and, since Android 13, the runtime permission too. */
    fun canPost(context: Context): Boolean = NotificationManagerCompat.from(context).areNotificationsEnabled()

    fun showMessage(context: Context) {
        val notification = NotificationCompat.Builder(context, CHANNEL_MESSAGES)
            .setSmallIcon(smallIcon(context))
            .setContentTitle(context.getString(R.string.notifications_message_title))
            .setContentText(context.getString(R.string.notifications_message_text))
            .setStyle(NotificationCompat.BigTextStyle().bigText(context.getString(R.string.notifications_message_long_text)))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            // Android 7 has no channels: the priority does the job of the channel importance.
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(openDemoIntent(context))
            .setAutoCancel(true)
            .addAction(0, context.getString(R.string.notifications_mark_as_read), markAsReadIntent(context))
            .build()
        notifyIfAllowed(context, MESSAGE_ID, notification)
    }

    fun showPicture(context: Context) {
        val picture = pictureBitmap(context)
        val notification = NotificationCompat.Builder(context, CHANNEL_PROMOTIONS)
            .setSmallIcon(smallIcon(context))
            .setContentTitle(context.getString(R.string.notifications_picture_title))
            .setContentText(context.getString(R.string.notifications_picture_text))
            .setLargeIcon(picture)
            // Expanded, the picture replaces the thumbnail and the summary replaces the text.
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(picture)
                    .bigLargeIcon(null as Bitmap?)
                    .setSummaryText(context.getString(R.string.notifications_picture_summary))
            )
            .setContentIntent(openDemoIntent(context))
            .setAutoCancel(true)
            .build()
        notifyIfAllowed(context, PICTURE_ID, notification)
    }

    /** Shows or updates the download notification; at 100 it turns into a dismissible "completed" one. */
    fun showDownload(context: Context, progress: Int) {
        val completed = progress >= 100
        val builder = NotificationCompat.Builder(context, CHANNEL_DOWNLOADS)
            .setSmallIcon(smallIcon(context))
            .setContentTitle(context.getString(R.string.notifications_download_title))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(openDemoIntent(context))
            // Updates don't make a sound or vibrate again.
            .setOnlyAlertOnce(true)
        if (completed) {
            builder.setContentText(context.getString(R.string.notifications_download_completed)).setAutoCancel(true)
        } else {
            builder.setContentText(context.getString(R.string.notifications_download_progress, progress))
                .setProgress(100, progress, false)
                .setOngoing(true)
        }
        notifyIfAllowed(context, DOWNLOAD_ID, builder.build())
    }

    fun cancelMessage(context: Context) {
        NotificationManagerCompat.from(context).cancel(MESSAGE_ID)
    }

    private fun channel(context: Context, id: String, importance: Int, name: Int, description: Int) =
        NotificationChannelCompat.Builder(id, importance)
            .setName(context.getString(name))
            .setDescription(context.getString(description))
            .build()

    private fun notifyIfAllowed(context: Context, id: Int, notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(id, notification)
    }

    private fun openDemoIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            // A fresh task with Home under the demo, whatever state the app was in.
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_OPEN_NOTIFICATIONS_DEMO, true)
        }
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun markAsReadIntent(context: Context): PendingIntent {
        val intent = Intent(context, NotificationActionReceiver::class.java).setAction(NotificationActionReceiver.ACTION_MARK_AS_READ)
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    /** The status bar only uses the alpha channel, so a white letter on transparency works as an icon. */
    private fun smallIcon(context: Context): IconCompat {
        val size = (24 * context.resources.displayMetrics.density).roundToInt()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = size.toFloat()
            typeface = Typeface.DEFAULT_BOLD
        }
        // Centers the letter itself, not the line of text it sits on.
        val bounds = Rect().also { paint.getTextBounds(MONOGRAM, 0, MONOGRAM.length, it) }
        val bitmap = createBitmap(size, size)
        Canvas(bitmap).drawText(MONOGRAM, size / 2f, size / 2f - bounds.exactCenterY(), paint)
        return IconCompat.createWithBitmap(bitmap)
    }

    private fun pictureBitmap(context: Context): Bitmap {
        val width = 720
        val height = 360
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        val background = Paint().apply {
            shader = LinearGradient(0f, 0f, width.toFloat(), height.toFloat(), 0xFF6200EE.toInt(), 0xFF03DAC5.toInt(), Shader.TileMode.CLAMP)
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), background)
        val text = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 56f
            typeface = Typeface.DEFAULT_BOLD
        }
        canvas.drawText(context.getString(R.string.notifications_picture_banner), 40f, height - 48f, text)
        return bitmap
    }
}
