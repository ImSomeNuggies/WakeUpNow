package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.myapplication.receivers.AlarmReceiver
import android.app.AlarmManager

// Untested
// Schedules alarms after rebooting the system
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Load the alarms from SharedPreferences using AlarmPreferences
            val alarmPreferences = AlarmPreferences(context)
            val savedAlarms = alarmPreferences.loadAlarms()

            // Reschedule all saved alarms
            for (alarm in savedAlarms) {
                scheduleAlarm(context, alarm)
            }
        }
    }

    // Untested, likely to have some issues
    private fun scheduleAlarm(context: Context, alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarm_name", alarm.name)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,  // Ensure unique pending intent for each alarm
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarm.ringTime.timeInMillis,
            pendingIntent
        )
    }
}
