package com.example.myapplication.schedulers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.myapplication.Alarm
import com.example.myapplication.receivers.AlarmReceiver

class AlarmScheduler(private val context: Context) {

    // Untested, likely to have some issues
    fun scheduleAlarm(alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarm_name", alarm.name)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.ringTime.timeInMillis, pendingIntent)
    }

    fun stopRingtoneIfPlaying() {
        AlarmReceiver.ringtone?.let {
            if (it.isPlaying) {
                it.stop()
                AlarmReceiver.ringtone = null
            }
        }
    }
}
