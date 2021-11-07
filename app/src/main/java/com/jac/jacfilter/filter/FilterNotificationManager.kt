package com.jac.jacfilter.filter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import com.jac.jacfilter.R

/** Notification's manager. */
class FilterNotificationManager {

    companion object {
        /** Notification channel id. */
        private val NOTIFICATION_CHANNEL_ID =
            FilterNotificationManager::class.java.name + ".NOTIFICATION_CHANNEL_ID"
        /** Notification channel id. */
        private val NOTIFICATION_ID =
            (FilterNotificationManager::class.java.name + ".NOTIFICATION_CHANNEL_ID").hashCode()
    }

    /**
     * Create notification channel if not existing, do nothing otherwise.
     * @param context the Android context to get notification manager from.
     */
    private fun createNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) != null) {
            // Notification channel already exist
            return
        }

        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        notificationManager.createNotificationChannel(mChannel)
    }

    /**
     * Destroy notification channel if exist, do nothing otherwise.
     * @param context the Android context to get notification manager from.
     */
    private fun destroyNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID)
    }

    /**
     * Enable the notification
     * @param service the service to enable notification from.
     */
    fun enable(service: Service) {
        createNotificationChannel(service)
        val notification: Notification = Notification.Builder(service,
            NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle(service.getText(R.string.notification_title))
            .setContentText(service.getText(R.string.notification_message))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        service.startForeground(NOTIFICATION_ID, notification)
    }

    /**
     * Disable the notification.
     * @param service the service to disable notification from.
     */
    fun disable(service: Service) {
        service.stopForeground(true)
        destroyNotificationChannel(service)
    }
}