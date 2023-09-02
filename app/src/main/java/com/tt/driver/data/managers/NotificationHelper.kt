package com.tt.driver.data.managers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.tt.driver.ui.components.main.MainActivity
import com.waysgroup.t7t_talbk_driver.R

object NotificationHelper {

    private const val LOCATION_NOTIFICATION_CHANNEL_ID = "343"

    private const val LOCATION_NOTIFICATION_CHANNEL_NAME = "LOCATION_UPDATE_CHANNEL"

    private const val DEFAULT_NOTIFICATION_CHANNEL_ID = "546"

    private const val DEFAULT_NOTIFICATION_CHANNEL = "DEFAULT_NOTIFICATION_CHANNEL"

    private const val NOTIFICATION_ID = 1254

    fun generateLocationOnGoingNotification(context: Context): Notification {
        val title = "Taht Talabak"
        val message = "your location is being tracked"
        createLocationNotificationChannel(context)
        return generateNotification(context, title, message, LOCATION_NOTIFICATION_CHANNEL_ID, true)
    }

    fun pushNotification(
        context: Context,
        title: String,
        content: String,
    ) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        createDefaultNotificationChannel(context)

        notificationManager.notify(
            NOTIFICATION_ID,
            generateNotification(context, title, content, DEFAULT_NOTIFICATION_CHANNEL_ID)
        )
    }

    private fun generateNotification(
        context: Context,
        title: String,
        content: String,
        channelId: String,
        isLocationOngoingNotification: Boolean = false
    ): Notification {

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(title)
            .setBigContentTitle(content)

        val launchActivityIntent = Intent(context, MainActivity::class.java)

        val launchActivityPendingIntent = PendingIntent.getActivity(
            context, 0, launchActivityIntent, FLAG_IMMUTABLE
        )

        val notificationCompatBuilder =
            NotificationCompat.Builder(context, channelId)

        return notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(context,R.color.blue))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(isLocationOngoingNotification)
            .setContentIntent(launchActivityPendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(Notification.PRIORITY_MAX)
            .build()
    }

    private fun createDefaultNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                DEFAULT_NOTIFICATION_CHANNEL_ID,
                DEFAULT_NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_MAX
            )

            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createLocationNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                LOCATION_NOTIFICATION_CHANNEL_ID,
                LOCATION_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}