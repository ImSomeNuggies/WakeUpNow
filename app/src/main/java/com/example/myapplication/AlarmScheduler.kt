package com.example.myapplication.schedulers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.myapplication.Alarm
import com.example.myapplication.receivers.AlarmReceiver
import java.util.Calendar

class AlarmScheduler(private val context: Context) {

    // Untested, likely to have some issues
    fun scheduleAlarm(alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarm_name", alarm.name)
            putExtra("alarm_id", alarm.id)
            putExtra("alarm_isActive", alarm.isActive)
            putExtra("alarm_periodicity", alarm.periodicity)
        }

        // We check for an already existing pending intent
        val pendingIntent = PendingIntent.getBroadcast(context, alarm.id, alarmIntent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)

        if (pendingIntent != null) {
            // If it exists we cancel it then create a new one
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }

        // Crear un nuevo PendingIntent para programar la alarma
        val newPendingIntent = PendingIntent.getBroadcast(context, alarm.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, alarm.ringTime.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, alarm.ringTime.get(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val now = Calendar.getInstance()

        // If the hour of the alarm has passed, we set it for the next day
        if (calendar.timeInMillis <= now.timeInMillis) {
            if (alarm.periodicity != "Una vez") {
                // If it's a daily alarm it will sound the next day, if it's a weekly, it will be determined later
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            } else if (alarm.periodicity == "Una vez") { //TODO Eliminar o modificar alarmas de una vez
                // If the alarm is a one time alarm we ignore it
                Log.d("AlarmaDatos", "La alarma '${alarm.name}' ya ha pasado y no se reprogramará.")
                return
            }
        }


        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,  // The calculated time for the next occurrence
            newPendingIntent
        )

        Log.d("AlarmaScheduler", "Id: ${alarm.id}, Name: ${alarm.name}")
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
