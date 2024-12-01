package com.example.myapplication.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.myapplication.repository.AlarmPreferences
import com.example.myapplication.repository.AlarmRepository

/**
 * BroadcastReceiver that handles system reboot to reschedule alarms.
 */
 // Untested
// Schedules alarms after rebooting the system
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Initialize repository and scheduler
            val alarmPreferences = AlarmPreferences(context)
            val alarmRepository = AlarmRepository(alarmPreferences)
            val alarmScheduler = AlarmScheduler(context)

            // Load saved alarms and reschedule them
            val savedAlarms = alarmRepository.getAlarms()
            for (alarm in savedAlarms) {
                alarmScheduler.scheduleAlarm(alarm)
            }
        }
    }
}

