package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatisticsViewModel : ViewModel() {

    // Datos de ejemplo (lista de tiempos y errores para simulación)
    private val sampleSolveTimes = listOf(30, 45, 10, 25, 60) // en segundos
    private val sampleErrors = listOf(1, 3, 0, 2, 4)

    private val _averageTimeToTurnOff = MutableLiveData<Int>()
    val averageTimeToTurnOff: LiveData<Int> get() = _averageTimeToTurnOff

    private val _maxTimeToTurnOff = MutableLiveData<Int>()
    val maxTimeToTurnOff: LiveData<Int> get() = _maxTimeToTurnOff

    private val _minTimeToTurnOff = MutableLiveData<Int>()
    val minTimeToTurnOff: LiveData<Int> get() = _minTimeToTurnOff

    private val _maxErrors = MutableLiveData<Int>()
    val maxErrors: LiveData<Int> get() = _maxErrors

    // LiveData para los tiempos de respuesta promedio por hora
    private val _hourlyResponseTimeData = MutableLiveData<Map<String, Float>>()
    val hourlyResponseTimeData: LiveData<Map<String, Float>> get() = _hourlyResponseTimeData

    init {
        calculateStatistics()
        loadSampleHourlyData()
    }

    // Función que calcula y actualiza las estadísticas
    private fun calculateStatistics() {
        _averageTimeToTurnOff.value = calculateAverageTime()
        _maxTimeToTurnOff.value = calculateMaxTime()
        _minTimeToTurnOff.value = calculateMinTime()
        _maxErrors.value = calculateMaxErrors()
    }

    // Función para calcular el tiempo promedio de apagado
    private fun calculateAverageTime(): Int {
        return if (sampleSolveTimes.isNotEmpty()) {
            sampleSolveTimes.sum() / sampleSolveTimes.size
        } else {
            0
        }
    }

    // Función para calcular el tiempo máximo de apagado
    private fun calculateMaxTime(): Int {
        return sampleSolveTimes.maxOrNull() ?: 0
    }

    // Función para calcular el tiempo mínimo de apagado
    private fun calculateMinTime(): Int {
        return sampleSolveTimes.minOrNull() ?: 0
    }

    // Función para calcular el número máximo de fallos
    private fun calculateMaxErrors(): Int {
        return sampleErrors.maxOrNull() ?: 0
    }

    // Cargar datos ficticios para el gráfico de tiempo de respuesta promedio por hora
    private fun loadSampleHourlyData() {
        // Datos de ejemplo en segundos (tiempos de respuesta promedio por hora)
        val sampleHourlyData = mapOf(
            "6:00" to 11.5f,
            "7:00" to 7.8f,
            "8:00" to 12.1f,
            "10:00" to 10.3f,
            "16:00" to 5.8f,
            "17:00" to 8.1f
        )

        _hourlyResponseTimeData.value = sampleHourlyData
    }
}
