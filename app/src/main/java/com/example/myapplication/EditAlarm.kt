package com.example.myapplication

import AlarmPreferences
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import java.util.*

class EditAlarm : ComponentActivity() {

    private lateinit var alarm: Alarm
    private var selectedTime: String = ""  // Inicializa selectedTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_alarm_layout)

        // Referencias a los componentes del layout
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val buttonPickTime = findViewById<Button>(R.id.buttonPickTime)
        val selectedTimeText = findViewById<TextView>(R.id.selectedTimeText)
        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val spinnerPeriodicidad = findViewById<Spinner>(R.id.spinnerPeriodicidad)
        val buttonConfirmar = findViewById<Button>(R.id.buttonConfirmar)
        val buttonBorrar = findViewById<Button>(R.id.buttonBorrar)

        // Recuperar la alarma desde SharedPreferences usando su ID
        val alarmId = intent.getIntExtra("alarm_id", -1)
        if (alarmId != -1) {
            val alarmPreferences = AlarmPreferences(this)
            alarm = alarmPreferences.getAlarmById(alarmId) ?: return
        }

        // Inicializa selectedTime con el valor de la alarma recuperada
        selectedTime = alarm.time  // Se obtiene el tiempo guardado en la alarma
        editTextNombre.setText(alarm.name)
        selectedTimeText.text = "Hora seleccionada: $selectedTime"
        val periodicityOptions = resources.getStringArray(R.array.periodicidad_options)
        spinnerPeriodicidad.setSelection(periodicityOptions.indexOf(alarm.periodicity))

        // Abrir TimePickerDialog al hacer clic en "Seleccionar Hora"
        buttonPickTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    selectedTimeText.text = "Hora seleccionada: $selectedTime"
                    alarm.time = selectedTime  // Actualizar la hora en la alarma
                    alarm.ringTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    alarm.ringTime.set(Calendar.MINUTE, minute)
                    alarm.ringTime.set(Calendar.SECOND, 0)
                    alarm.ringTime.set(Calendar.MILLISECOND, 0)
                },
                currentHour,
                currentMinute,
                true
            )
            timePickerDialog.show()
        }

        // Update alarm when clicking the "Confirm" button
        buttonConfirmar.setOnClickListener {
            // Get the alarm name and selected periodicity
            val alarmName = editTextNombre.text.toString()
            val periodicity = spinnerPeriodicidad.selectedItem.toString()

            // Check if a time has been selected
            if (selectedTime.isEmpty()) {
                Toast.makeText(this, "Seleccionar hora", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // We adapt the time from a string into a Calendar format
            val timeParts = selectedTime.split(":").map { it.toInt() }
            val hour = timeParts[0]
            val minute = timeParts[1]

            val updatedAlarmCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Load existing alarms from SharedPreferences
            val alarmPreferences = AlarmPreferences(this)
            // Update the existing alarm's fields
            alarm.name = alarmName
            alarm.periodicity = periodicity
            alarm.time = selectedTime
            alarm.ringTime = updatedAlarmCalendar

            // Save the updated alarm in SharedPreferences
            alarmPreferences.editAlarm(alarm)

            // Display a confirmation message
            Toast.makeText(this, "Alarma editada", Toast.LENGTH_SHORT).show()

            // Return to the main activity (MainActivity)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // End this activity to prevent going back to it
        }

        // Action for the Delete button to remove the alarm
        buttonBorrar.setOnClickListener {
            // Delete the alarm from SharedPreferences
            val alarmPreferences = AlarmPreferences(this)
            alarmPreferences.deleteAlarm(alarm.id)

            // Show a confirmation message
            Toast.makeText(this, "Alarm deleted", Toast.LENGTH_SHORT).show()

            // Navigate back to the main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // End this activity to prevent returning to it
        }

        // Acción del botón de volver (cruz)
        buttonBack.setOnClickListener {
            finish()  // Finaliza esta actividad y vuelve atrás
        }
    }
}
