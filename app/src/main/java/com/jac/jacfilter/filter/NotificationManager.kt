package com.jac.jacfilter.filter

import android.app.Notification
import android.app.Service
import com.jac.jacfilter.R

/** Notification's manager. */
class NotificationManager {

    companion object {
        /** Notification channel id. */
        private val NOTIFICATION_CHANNEL_ID =
            NotificationManager::class.java.name + ".NOTIFICATION_CHANNEL_ID"
        /** Notification channel id. */
        private val NOTIFICATION_ID =
            (NotificationManager::class.java.name + ".NOTIFICATION_CHANNEL_ID").hashCode()
    }

    /**
     * Enable the notification
     * @param service the service to enable notification from.
     */
    fun enable(service: Service) {

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
    }
}