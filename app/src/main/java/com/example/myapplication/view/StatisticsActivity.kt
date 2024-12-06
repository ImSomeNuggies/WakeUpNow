package com.example.myapplication.view

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.viewmodel.StatisticsViewModel
import com.example.myapplication.viewmodel.factory.StatisticsViewModelFactory
import com.example.myapplication.repository.AlarmStatsRepository

class StatisticsActivity : ComponentActivity() {

    private lateinit var viewModel: StatisticsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics_layout)

        // Initialize the repository and ViewModel with the factory
        val sharedPreferences = getSharedPreferences("alarms", Context.MODE_PRIVATE)
        val alarmStatsRepository = AlarmStatsRepository(sharedPreferences)
        val viewModelFactory = StatisticsViewModelFactory(alarmStatsRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[StatisticsViewModel::class.java]

        // Bind UI elements
        val avgTimeTextView: TextView = findViewById(R.id.avgTimeValue)
        val maxTimeTextView: TextView = findViewById(R.id.maxTimeValue)
        val minTimeTextView: TextView = findViewById(R.id.minTimeValue)
        val maxErrorsTextView: TextView = findViewById(R.id.maxErrorsValue)
        val barChartView = findViewById<BarChartView>(R.id.barChartView)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val graphTypeSpinner = findViewById<Spinner>(R.id.graphTypeSpinner)

        // Observe ViewModel LiveData and update UI with general statistics
        viewModel.averageTimeToTurnOff.observe(this) { avgTime ->
            avgTimeTextView.text = "Tiempo medio: $avgTime seg" // Show average turn-off time
        }

        viewModel.maxTimeToTurnOff.observe(this) { maxTime ->
            maxTimeTextView.text = "Tiempo máx.: $maxTime seg" // Show maximum turn-off time
        }

        viewModel.minTimeToTurnOff.observe(this) { minTime ->
            minTimeTextView.text = "Tiempo mín.: $minTime seg" // Show minimum turn-off time
        }

        viewModel.maxErrors.observe(this) { maxErrors ->
            maxErrorsTextView.text = "Máx. errores: $maxErrors" // Show maximum errors
        }

        // Set up the Spinner with a custom layout for the dropdown
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.graph_types, // Array resource for graph types
            R.layout.spinner_item  // Apply custom layout for spinner items
        ).apply {
            setDropDownViewResource(R.layout.spinner_item) // Use the same layout for the dropdown view
        }
        graphTypeSpinner.adapter = adapter

        // Select the first option (e.g., "Average Time") by default
        graphTypeSpinner.setSelection(0)

        // Set OnItemSelectedListener to update the chart based on the selected graph type
        graphTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val selectedType = parent.getItemAtPosition(position).toString() // Get selected graph type
                viewModel.getGraphData(selectedType).observe(this@StatisticsActivity) { data ->
                    barChartView.setData(data as Map<String, Float>) // Update the bar chart with data
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed when nothing is selected
            }
        }

        // Initialize the chart with the default option ("Average Time")
        viewModel.getGraphData("Tiempo medio (seg)").observe(this) { data ->
            barChartView.setData(data as Map<String, Float>)
        }

        // Set click listener for the back button to finish the activity
        buttonBack.setOnClickListener {
            finish()
        }
    }
}