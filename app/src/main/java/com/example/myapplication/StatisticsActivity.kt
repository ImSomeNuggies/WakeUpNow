package com.example.myapplication

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import com.example.myapplication.R

class StatisticsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics_layout)

        // Set up back button to return to the previous screen
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }
    }
}
