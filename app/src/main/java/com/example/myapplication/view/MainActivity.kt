package com.example.myapplication.view

import com.example.myapplication.viewmodel.helper.AlarmPermissionHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.viewmodel.helper.NotificationHelper
import com.example.myapplication.viewmodel.AlarmScheduler
import android.widget.Toast
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.model.Alarm
import com.example.myapplication.repository.AlarmPreferences
import com.example.myapplication.repository.AlarmRepository


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


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        alarmPermissionHelper = AlarmPermissionHelper(this, requestNotificationPermissionLauncher)
        alarmScheduler = AlarmScheduler(this)
        val sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE)
        alarmPreferences = AlarmPreferences(sharedPreferences)
        alarmRepository = AlarmRepository(alarmPreferences)

        alarmPermissionHelper.checkNotificationPermission()
        NotificationHelper.createNotificationChannel(this)

        val buttonCreateAlarm: Button = findViewById(R.id.buttonCreateAlarm)
        val buttonStatistics: ImageButton = findViewById(R.id.buttonStatistics)
        val recyclerViewAlarms = findViewById<RecyclerView>(R.id.recyclerViewAlarms)
        val buttonQR: ImageButton = findViewById(R.id.buttonQR)
        val alarmAdapter = AlarmAdapter(alarmList)

        recyclerViewAlarms.layoutManager = LinearLayoutManager(this)
        recyclerViewAlarms.adapter = alarmAdapter

        alarmList.clear()
        alarmList.addAll(alarmRepository.getAlarms())
        alarmAdapter.notifyDataSetChanged()

        for (alarm in alarmList) {
            // Log de los datos de la alarma
            Log.d("AlarmInfo", "ID: ${alarm.id}, Name: ${alarm.name}, Time: ${alarm.time}, Periodicity: ${alarm.periodicity}, Is Active: ${alarm.isActive}, Ring Time: ${alarm.ringTime.time}")

            // Programar la alarma
            alarmScheduler.scheduleAlarm(alarm)
        }

        buttonCreateAlarm.setOnClickListener {
            startActivity(Intent(this, CreateAlarmActivity::class.java))
        }

        buttonStatistics.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }


        buttonQR.setOnClickListener {
            startActivity(Intent(this, QrGeneratorActivity::class.java))
        }
    }



}