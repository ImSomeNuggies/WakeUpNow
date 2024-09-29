package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        // Find the button by its ID
        val buttonCreateAlarm: Button = findViewById(R.id.buttonCreateAlarm)

        // Set a click listener for the button
        buttonCreateAlarm.setOnClickListener {
            // Create an intent to start the CreateAlarm activity
            val intent = Intent(this, CreateAlarm::class.java)
            startActivity(intent)
        }
    }



}