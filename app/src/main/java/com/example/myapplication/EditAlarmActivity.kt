package com.example.myapplication

import android.app.TimePickerDialog
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
        val buttonPickTime = findViewById<Button>(R.id.buttonPickTime)
        val selectedTimeText = findViewById<TextView>(R.id.selectedTimeText)
        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val spinnerPeriodicidad = findViewById<Spinner>(R.id.spinnerPeriodicidad)
        val buttonConfirmar = findViewById<Button>(R.id.buttonConfirmar)
        val buttonBorrar = findViewById<Button>(R.id.buttonBorrar)

        // Cargar alarma desde el ViewModel
        val alarmId = intent.getIntExtra("alarm_id", -1)
        if (alarmId != -1) {
            viewModel.loadAlarm(alarmId)
            val alarm = viewModel.alarm
            selectedTime = alarm?.time ?: ""
            editTextNombre.setText(alarm?.name ?: "")
            selectedTimeText.text = "Hora seleccionada: $selectedTime"
            val periodicityOptions = resources.getStringArray(R.array.periodicidad_options)
            spinnerPeriodicidad.setSelection(periodicityOptions.indexOf(alarm?.periodicity ?: ""))
        }

        // Abrir TimePickerDialog
        buttonPickTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    selectedTimeText.text = "Hora seleccionada: $selectedTime"
                },
                currentHour,
                currentMinute,
                true
            )
            timePickerDialog.show()
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
