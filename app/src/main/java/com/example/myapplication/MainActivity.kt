package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.helpers.AlarmPermissionHelper
import com.example.myapplication.helpers.NotificationHelper
import com.example.myapplication.AlarmRepository
import com.example.myapplication.schedulers.AlarmScheduler
import android.widget.Toast

class MainActivity : ComponentActivity() {
    private val alarmList = mutableListOf<Alarm>()
    private lateinit var alarmPermissionHelper: AlarmPermissionHelper
    private lateinit var alarmScheduler: AlarmScheduler
    private lateinit var alarmPreferences: AlarmPreferences
    private lateinit var alarmRepository: AlarmRepository

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onResume() {
        super.onResume()
        alarmScheduler.stopRingtoneIfPlaying()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        alarmPermissionHelper = AlarmPermissionHelper(this, requestNotificationPermissionLauncher)
        alarmScheduler = AlarmScheduler(this)
        alarmPreferences = AlarmPreferences(this)
        alarmRepository = AlarmRepository(alarmPreferences)

        alarmPermissionHelper.checkNotificationPermission()
        NotificationHelper.createNotificationChannel(this)

        val buttonCreateAlarm: Button = findViewById(R.id.buttonCreateAlarm)
        val buttonStatistics: ImageButton = findViewById(R.id.buttonStatistics)
        val recyclerViewAlarms = findViewById<RecyclerView>(R.id.recyclerViewAlarms)
        val alarmAdapter = AlarmAdapter(alarmList)

        recyclerViewAlarms.layoutManager = LinearLayoutManager(this)
        recyclerViewAlarms.adapter = alarmAdapter

        alarmList.clear()
        alarmList.addAll(alarmRepository.getAlarms())
        alarmAdapter.notifyDataSetChanged()

        buttonCreateAlarm.setOnClickListener {
            startActivity(Intent(this, CreateAlarm::class.java))
        }

        buttonStatistics.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }
    }
}