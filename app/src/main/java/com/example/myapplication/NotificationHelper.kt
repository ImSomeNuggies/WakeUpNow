package com.example.myapplication.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.AlarmSoundingActivity
import com.example.myapplication.R

/**
 * Helper class for managing notification channels and displaying notifications.
 */
object NotificationHelper {

    /**
     * Creates a high-priority notification channel (required for Android 8.0+).
     *
     * @param context The context used to create the notification channel.
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "High Priority Channel"
            val descriptionText = "This is used for high priority notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("high_priority_channel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Shows a high-priority notification with an action to open `AlarmSoundingActivity`.
     *
     * @param context The context for accessing system services.
     * @param name The name of the alarm to display in the notification.
     * @param id The unique ID for the notification.
     */
    fun showHighPriorityNotification(context: Context, name: String?, id: Int) {
        // Intent to open the activity when tapped
        val intent = Intent(context, AlarmSoundingActivity::class.java).apply {
            putExtra("alarm_name", name)
            putExtra("launched_from_notification", true)  // Add this line
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, id, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build the notification
        val builder = NotificationCompat.Builder(context, "high_priority_channel")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(name)
            .setContentText("Pulse para parar la alarma")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // Show the notification
        with(NotificationManagerCompat.from(context)) { // We need to ensure notification permissions are on
            notify(123, builder.build())  // 123 is the notification ID
        }
    }
}
