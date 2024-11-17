package com.example.myapplication

import android.widget.TimePicker
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.AlarmPreferences
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import java.util.*

class EditAlarmActivity : ComponentActivity() {

    private lateinit var viewModel: EditAlarmViewModel
    private var selectedTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_alarm_layout)

        // Inicializa el ViewModel
        val alarmPreferences = AlarmPreferences(this)
        viewModel = ViewModelProvider(this, EditAlarmViewModelFactory(alarmPreferences)).get(EditAlarmViewModel::class.java)

        // Referencias a los componentes del layout
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val spinnerPeriodicidad = findViewById<Spinner>(R.id.spinnerPeriodicidad)
        val buttonConfirmar = findViewById<Button>(R.id.buttonConfirmar)
        val buttonBorrar = findViewById<Button>(R.id.buttonBorrar)

        // Load alarm from the ViewModel
        val alarmId = intent.getIntExtra("alarm_id", -1)
        if (alarmId != -1) {
            viewModel.loadAlarm(alarmId)
            val alarm = viewModel.alarm
            selectedTime = alarm?.time ?: ""
            editTextNombre.setText(alarm?.name ?: "")

            // Set the initial time in TimePicker
            timePicker.setIs24HourView(true)
            if (selectedTime.isNotEmpty()) {
                val parts = selectedTime.split(":")
                val initialHour = parts[0].toInt()
                val initialMinute = parts[1].toInt()
                timePicker.hour = initialHour
                timePicker.minute = initialMinute
            } else {
                // Fallback to the current time if no time is set
                val calendar = Calendar.getInstance()
                timePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
                timePicker.minute = calendar.get(Calendar.MINUTE)
            }

            // Update the TextView to show the initially selected time
            val periodicityOptions = resources.getStringArray(R.array.periodicidad_options)
            spinnerPeriodicidad.setSelection(periodicityOptions.indexOf(alarm?.periodicity ?: ""))
        }

        // Set a listener to capture time changes in the TimePicker
        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        }

        // Actualizar alarma al hacer clic en "Confirmar"
        buttonConfirmar.setOnClickListener {
            val alarmName = editTextNombre.text.toString()
            val periodicity = spinnerPeriodicidad.selectedItem.toString()

            if (selectedTime.isEmpty()) {
                Toast.makeText(this, "Seleccionar hora", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Actualizar alarma a través del ViewModel
            viewModel.updateAlarm(alarmName, selectedTime, periodicity)

            Toast.makeText(this, "Alarma editada", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Eliminar alarma al hacer clic en "Borrar"
        buttonBorrar.setOnClickListener {
            viewModel.deleteAlarm()
            Toast.makeText(this, "Alarm deleted", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Acción del botón de volver
        buttonBack.setOnClickListener {
            finish()
        }
    }
}
