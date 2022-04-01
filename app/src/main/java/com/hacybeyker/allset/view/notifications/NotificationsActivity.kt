package com.hacybeyker.allset.view.notifications

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.service.notification.StatusBarNotification
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.snackbar.Snackbar
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.R
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityNotificationsBinding
import com.hacybeyker.allset.view.libraries.scanner.LibraryScannerZxingActivity
import com.hacybeyker.allset.view.notifications.adapter.NotificationChannelAdapter
import com.hacybeyker.allset.view.notifications.adapter.NotificationStatusBarAdapter
import com.hacybeyker.allset.view.notifications.vo.NotificationVO
import com.hacybeyker.allset.view.viewpager.onboarding.ViewPagerOnboardingActivity
import kotlin.random.Random

class NotificationsActivity : BaseActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var item: Item
    private val adapterNotificationChannels by lazy { NotificationChannelAdapter() }
    private val adapterNotificationStatusBar by lazy { NotificationStatusBarAdapter() }
    private val notificationManager: NotificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    companion object {

        private val tag = ViewPagerOnboardingActivity::class.java.simpleName
        private const val CHANNEL_ID_ONE = "Channel1"
        private const val CHANNEL_NAME_ONE = "Notification Channel 1"
        private const val CHANNEL_DESCRIPTION_ONE = "This notification is used to notify user1"
        private const val NOTIFY_ID_ONE = 1

        private const val CHANNEL_ID_TWO = "Channel2"
        private const val CHANNEL_NAME_TWO = "Notification Channel 2"
        private const val CHANNEL_DESCRIPTION_TWO = "This notification is used to notify user2"
        private const val NOTIFY_ID_TWO = 2

        private const val CHANNEL_ID_THREE = "Channel3"
        private const val CHANNEL_NAME_THREE = "Notification Channel 3"
        private const val CHANNEL_DESCRIPTION_THREE = "This notification is used to notify user3"
        private const val NOTIFY_ID_THREE = 3

        private const val CHANNEL_ID_FOUR = "Channel4"
        private const val CHANNEL_NAME_FOUR = "Notification Channel 4"
        private const val CHANNEL_DESCRIPTION_FOUR = "This notification is used to notify user4"
        private const val NOTIFY_ID_FOUR = 4

        private const val REQUEST_CODE = 0

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, NotificationsActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        binding.tvDescription.text = item.description
        setupListeners()
        setupAdapters()
    }

    private fun getIntentData() {
        if (intent != null) {
            val dataTemp = intent.getParcelableExtra<Item>(tag)
            if (dataTemp != null)
                item = dataTemp
        }
    }

    private fun setupListeners() {
        with(binding) {
            mbNotificationNormal.setOnClickListener {
                generateNotificationNormal()
            }

            mbNotificationWithIntent.setOnClickListener {
                generateNotificationWithIntent()
            }

            mbNotificationWithImage.setOnClickListener {
                generateNotificationWithImage()
            }

            mbNotificationCustom.setOnClickListener {
                generateNotificationCustom()
            }

            mbNotificationStatus.setOnClickListener {
                if (!isNotificationGlobalEnabled(applicationContext)) {
                    showSnackBarConfigureNotifications()
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    adapterNotificationChannels.items =
                        getNotificationChannels(applicationContext).map {
                            NotificationVO(
                                notificationId = it.id,
                                notificationName = it.name.toString(),
                                notificationImportance = it.importance
                            )
                        } as ArrayList<NotificationVO>

                    adapterNotificationStatusBar.items =
                        getNotificationsActiveFromStatusBar(applicationContext).map {
                            //it.id.toString()
                            it.notification.channelId.toString()
                        } as ArrayList<String>
                }
            }
        }
    }

    private fun setupAdapters() {
        binding.rvNotificationChannel.adapter = adapterNotificationChannels
        binding.rvNotificationStatusBar.adapter = adapterNotificationStatusBar
    }

    private fun showNotificationChannelActives(context: Context) {
        for (channel in getNotificationChannels(context)) {

        }
    }

    private fun showSnackBarConfigureNotifications() {
        Snackbar.make(binding.containerMain, "Notificaciones desactivadas", Snackbar.LENGTH_LONG)
            .setAction("Activar") {
                val intent = Intent().apply {
                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                        }
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                            action = "android.settings.APP_NOTIFICATION_SETTINGS"
                            putExtra("app_package", packageName)
                            putExtra("app_uid", applicationInfo.uid)
                        }
                        else -> {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            addCategory(Intent.CATEGORY_DEFAULT)
                            data = Uri.parse("package:$packageName")
                        }
                    }
                }
                startActivity(intent)
            }.show()
    }

    private fun getNotificationsActiveFromStatusBar(context: Context): List<StatusBarNotification> {
        return if (isNotificationGlobalEnabled(context)) {
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            nm.activeNotifications.asList()
        } else {
            arrayListOf()
        }
    }

    private fun getNotificationChannels(context: Context): List<NotificationChannel> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.notificationChannels
        } else {
            arrayListOf()
        }
    }

    private fun isNotificationEnabledByChannel(context: Context, channelId: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = nm.getNotificationChannel(channelId)
            channel.importance != NotificationManager.IMPORTANCE_NONE
        } else {
            false
        }
    }

    private fun isNotificationGlobalEnabled(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    private fun setupNotifications() {
        /*----Resources----*/
        //Github:
        //Page:
        //References:
        //Dependencies:
        //Resources:
        //https://developer.android.com/guide/topics/ui/notifiers/notifications
        //https://developer.android.com/training/notify-user/channels
        //https://developer.android.com/training/notify-user/navigation
        //https://stackoverflow.com/questions/32366649/any-way-to-link-to-the-android-notification-settings-for-my-app

    }

    private fun generateNotificationNormal() {
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID_ONE).apply {
            setSmallIcon(R.drawable.icon_notifications)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            setContentTitle("What is Lorem Ipsum?")
            setContentText("Lorem Ipsum is simply dummy text...")
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_MAX
        }
        showNotification(
            CHANNEL_ID_ONE,
            CHANNEL_NAME_ONE,
            CHANNEL_DESCRIPTION_ONE,
            NOTIFY_ID_ONE,
            builder
        )
    }

    private fun generateNotificationWithIntent() {
        val intent = Intent(applicationContext, LibraryScannerZxingActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID_TWO).apply {
            setSmallIcon(R.drawable.icon_notifications)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            setContentTitle("What is Lorem Ipsum?")
            setContentText("Lorem Ipsum is simply dummy text...")
            setContentIntent(pendingIntent)
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_MAX
        }
        showNotification(
            CHANNEL_ID_TWO,
            CHANNEL_NAME_TWO,
            CHANNEL_DESCRIPTION_TWO,
            NOTIFY_ID_TWO,
            builder
        )
    }

    private fun generateNotificationWithImage() {
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID_THREE).apply {
            setSmallIcon(R.drawable.icon_notifications)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            setContentTitle("What is Lorem Ipsum?")
            setContentText("Lorem Ipsum is simply dummy text...")
            setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.")
            )
            setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(
                    ContextCompat.getDrawable(applicationContext, R.drawable.scene1)?.toBitmap()
                )
            )
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_MAX
        }
        showNotification(
            CHANNEL_ID_THREE,
            CHANNEL_NAME_THREE,
            CHANNEL_DESCRIPTION_THREE,
            NOTIFY_ID_THREE,
            builder
        )
    }

    private fun generateNotificationCustom() {
        ///Nota las vistas cargadas en RemoteViews no soporta ConstrainttLayout y ningun componente interno de tipo androidx
        //https://stackoverflow.com/questions/45396426/crash-when-using-constraintlayout-in-notification
        //https://developer.android.com/guide/topics/appwidgets/index.html#CreatingLayout
        val collapsedView = RemoteViews(packageName, R.layout.notification_custom_collapsed)
        val expandedView = RemoteViews(packageName, R.layout.notification_custom_expanded)

        //Se puede cambiar el valor de los atributos de esta forma
        /*collapsedView.setTextViewText(R.id.tvNotificationCustomCollapsedTitle, "Helouda!")
        expandedView.setImageViewResource(R.id.tvNotificationCustomExpandedImage, R.drawable.scene3)*/

        val intent = Intent(applicationContext, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
        expandedView.setOnClickPendingIntent(R.id.ivNotificationCustomExpandedImage, pendingIntent)

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID_FOUR).apply {
            setSmallIcon(R.drawable.icon_notifications)
            setCustomContentView(collapsedView)
            setCustomBigContentView(expandedView)
            setStyle(NotificationCompat.DecoratedCustomViewStyle())
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_MAX
        }
        showNotification(
            CHANNEL_ID_FOUR,
            CHANNEL_NAME_FOUR,
            CHANNEL_DESCRIPTION_FOUR,
            NOTIFY_ID_FOUR,
            builder
        )
    }

    private fun showNotification(
        channelId: String,
        channelName: String,
        channelDescription: String?,
        notificationId: Int,
        builder: NotificationCompat.Builder
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = channelDescription ?: ""
                    enableVibration(true)
                    enableLights(true)
                    lightColor = Color.GREEN
                }
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        val notification = builder.build()
        if (notification != null) {
            notificationManager.notify(notificationId, notification)
        }
    }

    private fun generateIntRandom(): Int {
        return Random.nextInt(100)
    }

}