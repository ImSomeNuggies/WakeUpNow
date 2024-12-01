package com.example.myapplication.viewmodel.helper

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher

class AlarmPermissionHelper(
    private val activity: Activity,
    private val requestNotificationPermissionLauncher: ActivityResultLauncher<String>
) {

    fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (activity.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                Toast.makeText(activity, "Notification permission already granted", Toast.LENGTH_SHORT).show()
            }
            checkAndRequestAlarmPermission()
        } else {
            Toast.makeText(activity, "Notification permission granted by default", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(activity, "Please allow exact alarm permission in settings", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                activity.startActivity(intent)
            } else {
                Toast.makeText(activity, "Alarm permission already accepted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
