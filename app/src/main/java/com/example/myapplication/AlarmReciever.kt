package com.example.myapplication.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.CreateAlarm
import com.example.myapplication.MainActivity
import com.example.myapplication.R


class AlarmReceiver : BroadcastReceiver() {

    companion object {
        var ringtone: Ringtone? = null
    }

    override fun onReceive(context: Context, intent: Intent) {

        val alarmName = intent.getStringExtra("alarm_name")
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, alarmUri)
        // Llama a la interfaz para que suene y se vea la alamra
        ringtone?.play()
        // Muestra un mensaje cuando suena la alarma
        Toast.makeText(context, "¡Alarma sonando!", Toast.LENGTH_LONG).show()
        showHighPriorityNotification(context, alarmName)



    }

    fun showHighPriorityNotification(context: Context, name: String?) {
        // Intent to open the activity when tapped
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
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

