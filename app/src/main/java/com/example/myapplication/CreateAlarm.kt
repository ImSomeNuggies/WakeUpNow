package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
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
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.AlarmPreferences
import com.example.myapplication.receivers.AlarmReceiver
import java.util.*

class CreateAlarm : ComponentActivity() {

    private lateinit var viewModel: CreateAlarmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_alarm_layout)

        val alarmPreferences = AlarmPreferences(this)
        val repository = AlarmRepository(alarmPreferences)
        val viewModelFactory = CreateAlarmViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[CreateAlarmViewModel::class.java]

        val buttonPickTime = findViewById<Button>(R.id.buttonPickTime)
        val selectedTimeText = findViewById<TextView>(R.id.selectedTimeText)
        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val spinnerPeriodicidad = findViewById<Spinner>(R.id.spinnerPeriodicidad)
        val buttonConfirmar = findViewById<Button>(R.id.buttonConfirmar)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)

        buttonPickTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    viewModel.selectedTime = selectedTime
                    selectedTimeText.text = "Hora seleccionada: $selectedTime"
                },
                currentHour,
                currentMinute,
                true
            )
            timePickerDialog.show()
        }

        buttonConfirmar.setOnClickListener {
            val alarmName = editTextNombre.text.toString()
            viewModel.selectedPeriodicity = spinnerPeriodicidad.selectedItem.toString()

            try {
                var newAlarm = viewModel.saveAlarm(alarmName)

                val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmIntent = Intent(this, AlarmReceiver::class.java).apply {
                    putExtra("alarm_name", newAlarm.name)
                }
                val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    newAlarm.ringTime.timeInMillis,
                    pendingIntent
                )


                Toast.makeText(this, "Alarma guardada", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        buttonBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
