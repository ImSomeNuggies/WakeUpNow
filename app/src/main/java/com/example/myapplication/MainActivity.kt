package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
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

class MainActivity : ComponentActivity() {
    private val alarmList = mutableListOf<Alarm>() // Para probar el RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        // Find the button by its ID
        val buttonCreateAlarm: Button = findViewById(R.id.buttonCreateAlarm)

        // Set up RecyclerView
        val recyclerViewAlarms = findViewById<RecyclerView>(R.id.recyclerViewAlarms)
        val alarmAdapter = AlarmAdapter(alarmList)
        recyclerViewAlarms.layoutManager = LinearLayoutManager(this)
        recyclerViewAlarms.adapter = alarmAdapter

        // Load existing alarms (if any)
        loadAlarms()

        // Set a click listener for the button
        buttonCreateAlarm.setOnClickListener {
            // Create an intent to start the CreateAlarm activity
            val intent = Intent(this, CreateAlarm::class.java)
            startActivity(intent)
        }
    }

    // ejemplos de prueba para el RecyclerView
    private fun loadAlarms() {
        // This method can be modified to load alarms from a database or shared preferences
        alarmList.add(Alarm("08:00", "Wake Up", "Diaria", true))
        alarmList.add(Alarm("09:00", "Morning Meeting", "Martes", false))

        // Notify adapter about data changes
        (findViewById<RecyclerView>(R.id.recyclerViewAlarms).adapter as AlarmAdapter).notifyDataSetChanged()
    }
}
