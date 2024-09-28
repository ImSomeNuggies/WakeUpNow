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
import android.widget.Button
import android.widget.TextView
import java.util.*

class CreateAlarm : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_alarm_layout)

        val buttonPickTime = findViewById<Button>(R.id.buttonPickTime)
        val selectedTimeText = findViewById<TextView>(R.id.selectedTimeText)

        buttonPickTime.setOnClickListener {
            // Get the current time
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            // Create and show TimePickerDialog
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    // When time is selected, display it in the TextView
                    selectedTimeText.text = "Hora seleccionada: $hourOfDay:$minute"
                },
                currentHour,
                currentMinute,
                true // Use 24-hour format
            )

            timePickerDialog.show()
        }
    }
}

