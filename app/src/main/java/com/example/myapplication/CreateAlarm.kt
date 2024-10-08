package com.example.myapplication

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
import android.content.Intent
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import java.util.*

class CreateAlarm : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_alarm_layout)

       // References to layout elements
        val buttonPickTime = findViewById<Button>(R.id.buttonPickTime)
        val selectedTimeText = findViewById<TextView>(R.id.selectedTimeText)
        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val spinnerPeriodicidad = findViewById<Spinner>(R.id.spinnerPeriodicidad)
        val buttonConfirmar = findViewById<Button>(R.id.buttonConfirmar)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)

        // List to store alarms
        val alarmList = mutableListOf<Alarm>()

        // Variable to store the selected time
        var selectedTime = ""

        // Open TimePickerDialog when clicking "Select Time" button
        buttonPickTime.setOnClickListener {
            // Get the current time
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            // Create and show TimePickerDialog
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    // Format the selected hour and minute with two digits
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    selectedTimeText.text = "Hora seleccionada: $selectedTime"
                },
                currentHour,
                currentMinute,
                true // Use 24-hour format
            )

            timePickerDialog.show()
        }

        // Save alarm when clicking the "Confirm" button
        buttonConfirmar.setOnClickListener {
            // Get the alarm name and selected periodicity
            val alarmName = editTextNombre.text.toString()
            val periodicity = spinnerPeriodicidad.selectedItem.toString()

            // Check if a time has been selected
            if (selectedTime.isEmpty()) {
                Toast.makeText(this, "Seleccionar hora", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a new Alarm instance
            val newAlarm = Alarm(
                time = selectedTime,
                name = alarmName,
                periodicity = periodicity,
                isActive = true
            )

            // Add the alarm to the list (you could also save it in persistent storage)
            alarmList.add(newAlarm)

            // Display a confirmation message
            Toast.makeText(this, "Alarma guardada", Toast.LENGTH_SHORT).show()

            // Return to the main activity (MainActivity)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // End this activity to prevent going back to it
        }

        // Handle the back button click to return to MainActivity without saving
        buttonBack.setOnClickListener {
            // Create an intent to return to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // End this activity to prevent going back to it
        }
    }
}

