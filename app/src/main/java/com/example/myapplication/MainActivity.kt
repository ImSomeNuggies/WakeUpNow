package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.receivers.AlarmReceiver
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.Calendar
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import android.Manifest
import android.provider.Settings





class MainActivity : ComponentActivity() {
    private val alarmList = mutableListOf<Alarm>() // Para probar el RecyclerView

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkAndRequestAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // For API 31 or higher
            if (checkSelfPermission(Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted; redirect to settings
                Toast.makeText(this, "Please allow exact alarm permission in settings", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            } else {
                // Permission granted, set the alarm
                Toast.makeText(this, "Alarm permission already accepted", Toast.LENGTH_SHORT).show()
            }
        } else {
            // If the API level is below 31, you can set the alarm directly
            Toast.makeText(this, "Alarm permission by default", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()

        //TODO: TEMPORARY, SHOULD BE MOVED TO THE RESOLUTION ACTIVITY
        // Stop the ringtone if it's still playing
        AlarmReceiver.ringtone?.let {
            if (it.isPlaying) {
                it.stop()
                AlarmReceiver.ringtone = null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)


        // Check for notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 and above
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request the notification permission
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // Permission is already granted
                Toast.makeText(this, "Notification permission already granted", Toast.LENGTH_SHORT).show()
            }
            if (checkSelfPermission(Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
            }
                // TODO Uncomment, commented since it asks nonstop for permission, needs a fix
                // checkAndRequestAlarmPermission()            }
            else{
                Toast.makeText(this, "Alarm permission already granted", Toast.LENGTH_SHORT).show()
            }
        } else {
            // For Android 12 and below: Notifications are granted by default
            Toast.makeText(this, "Notification permission granted by default", Toast.LENGTH_SHORT).show()
        }

        createNotificationChannel(this)

        //TODO: TEMPORARY, SHOULD BE MOVED TO THE RESOLUTION ACTIVITY
        // Stop the ringtone if it's still playing
        AlarmReceiver.ringtone?.let {
            if (it.isPlaying) {
                it.stop()
                AlarmReceiver.ringtone = null
            }
        }

        // Find the button by its ID
        val buttonCreateAlarm: Button = findViewById(R.id.buttonCreateAlarm)

        // Set up RecyclerView
        val recyclerViewAlarms = findViewById<RecyclerView>(R.id.recyclerViewAlarms)
        val alarmAdapter = AlarmAdapter(alarmList)
        recyclerViewAlarms.layoutManager = LinearLayoutManager(this)
        recyclerViewAlarms.adapter = alarmAdapter

        // Load existing alarms (if any)
        loadAlarms()

        // Set a click listener for the button
        buttonCreateAlarm.setOnClickListener {
            // Create an intent to start the CreateAlarm activity
            val intent = Intent(this, CreateAlarm::class.java)
            startActivity(intent)
        }
    }

    // ejemplos de prueba para el RecyclerView
    private fun loadAlarms() {

        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8) // Set the hour
            set(Calendar.MINUTE, 0)     // Set the minute
            set(Calendar.SECOND, 0)          // Set seconds to 0
            set(Calendar.MILLISECOND, 0)     // Set milliseconds to 0
        }

        // This method can be modified to load alarms from a database or shared preferences
        alarmList.add(Alarm("08:00", "Wake Up", "Diaria", true, ringTime =         Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8) // Set the hour
            set(Calendar.MINUTE, 0)     // Set the minute
            set(Calendar.SECOND, 0)          // Set seconds to 0
            set(Calendar.MILLISECOND, 0)     // Set milliseconds to 0
        } ))
        alarmList.add(Alarm("09:00", "Morning Meeting", "Martes", false, ringTime =         Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9) // Set the hour
            set(Calendar.MINUTE, 0)     // Set the minute
            set(Calendar.SECOND, 0)          // Set seconds to 0
            set(Calendar.MILLISECOND, 0)     // Set milliseconds to 0
        }))

        // Notify adapter about data changes
        (findViewById<RecyclerView>(R.id.recyclerViewAlarms).adapter as AlarmAdapter).notifyDataSetChanged()
    }


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


}
