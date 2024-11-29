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
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

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
        val btnLectorQR = findViewById<Button>(R.id.buttonqr)
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
            startActivity(Intent(this, CreateAlarm::class.java))
        }

        buttonStatistics.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }

        // Configurar el botón para abrir el lector QR
        btnLectorQR.setOnClickListener {
            iniciarLectorQR()
        }

        buttonQR.setOnClickListener {
            startActivity(Intent(this, QrGenerator::class.java))
        }
    }

    // Configuración del lector de QR
    private fun iniciarLectorQR() {
        val options = ScanOptions()
        options.setPrompt("Escanea un código QR")
        options.setBeepEnabled(true)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(true) // Bloquea la orientación en vertical
        options.setCaptureActivity(CustomScannerActivity::class.java);

        qrLauncher.launch(options)
    }

    // Manejar el resultado del lector de QR
    private val qrLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            Toast.makeText(this, "QR Escaneado: ${result.contents}", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
        }
    }


}