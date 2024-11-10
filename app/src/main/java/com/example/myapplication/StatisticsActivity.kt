package com.example.myapplication

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
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
        val graphTypeSpinner = findViewById<Spinner>(R.id.graphTypeSpinner)

        // Mostrar estadísticas generales
        viewModel.averageTimeToTurnOff.observe(this) { avgTime ->
            avgTimeTextView.text = "Tiempo medio: $avgTime seg"
        }

        viewModel.maxTimeToTurnOff.observe(this) { maxTime ->
            maxTimeTextView.text = "Tiempo máx.: $maxTime seg"
        }

        viewModel.minTimeToTurnOff.observe(this) { minTime ->
            minTimeTextView.text = "Tiempo mín.: $minTime seg"
        }

        viewModel.maxErrors.observe(this) { maxErrors ->
            maxErrorsTextView.text = "Máx. errores: $maxErrors"
        }

        // Configurar el Spinner con el layout personalizado
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.graph_types,
            R.layout.spinner_item  // Aplicar el layout personalizado para los elementos
        ).apply {
            setDropDownViewResource(R.layout.spinner_item) // También aplicarlo para el desplegable
        }
        graphTypeSpinner.adapter = adapter

        // Seleccionar la primera opción (e.g., "Average Time") de manera predeterminada
        graphTypeSpinner.setSelection(0)

        // Configurar el OnItemSelectedListener para actualizar el gráfico cuando se selecciona un tipo
        graphTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val selectedType = parent.getItemAtPosition(position).toString()
                viewModel.getGraphData(selectedType).observe(this@StatisticsActivity) { data ->
                    barChartView.setData(data as Map<String, Float>)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se necesita hacer nada
            }
        }

        // Inicializar el gráfico con la opción predeterminada
        viewModel.getGraphData("Tiempo medio (seg)").observe(this) { data ->
            barChartView.setData(data as Map<String, Float>)
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }
}
