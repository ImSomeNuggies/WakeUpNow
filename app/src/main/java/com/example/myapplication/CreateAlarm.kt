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
import android.util.Log

class CreateAlarm : ComponentActivity() {

    private lateinit var viewModel: CreateAlarmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_alarm_layout)

        val repository = AlarmRepository(this)
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
                    putExtra("alarm_id", newAlarm.id)
                    putExtra("alarm_isActive", newAlarm.isActive)
                    putExtra("alarm_periodicity", newAlarm.periodicity)
                    putExtra("alarm_ringTime", newAlarm.ringTime)
                    putExtra("alarm_time", newAlarm.time)
                }
                val pendingIntent = PendingIntent.getBroadcast(this, newAlarm.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                Log.d("AlarmaDatos", "Día de la semana: ${newAlarm.periodicity}")


                // Create a Calendar instance
                val calendar = Calendar.getInstance()

                // Set the time to the alarm's time
                calendar.set(Calendar.HOUR_OF_DAY, newAlarm.ringTime.get(Calendar.HOUR_OF_DAY))
                calendar.set(Calendar.MINUTE, newAlarm.ringTime.get(Calendar.MINUTE))
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                // Set the alarm to repeat every week on the specified day
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,  // The calculated time for the next occurrence
                    pendingIntent
                )

                Log.d("AlarmaDatos", "Próxima activación: ${calendar.time}")
                Log.d("AlarmaDatos", "Programando alarma:")
                Log.d("AlarmaDatos", "Nombre: ${newAlarm.name}")
                Log.d("AlarmaDatos", "Hora: ${newAlarm.ringTime.get(Calendar.HOUR_OF_DAY)}:${newAlarm.ringTime.get(Calendar.MINUTE)}")
                Log.d("AlarmaDatos", "Día de la semana: ${newAlarm.periodicity}")



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
