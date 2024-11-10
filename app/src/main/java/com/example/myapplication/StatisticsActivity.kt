package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.viewModels

class StatisticsActivity : ComponentActivity() {

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics_layout)

        val avgTimeTextView: TextView = findViewById(R.id.avgTimeValue)
        val maxTimeTextView: TextView = findViewById(R.id.maxTimeValue)
        val minTimeTextView: TextView = findViewById(R.id.minTimeValue)
        val maxErrorsTextView: TextView = findViewById(R.id.maxErrorsValue)
        val barChartView = findViewById<BarChartView>(R.id.barChartView)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)

        viewModel.averageTimeToTurnOff.observe(this) { avgTime ->
            avgTimeTextView.text = "Average Time: $avgTime sec"
        }

        viewModel.maxTimeToTurnOff.observe(this) { maxTime ->
            maxTimeTextView.text = "Max Time: $maxTime sec"
        }

        viewModel.minTimeToTurnOff.observe(this) { minTime ->
            minTimeTextView.text = "Min Time: $minTime sec"
        }

        viewModel.maxErrors.observe(this) { maxErrors ->
            maxErrorsTextView.text = "Max Errors: $maxErrors"
        }

        viewModel.hourlyResponseTimeData.observe(this) { hourlyData ->
            barChartView.setData(hourlyData)
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }
}
