package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatisticsViewModel : ViewModel() {

    // Datos generales
    private val _averageTimeToTurnOff = MutableLiveData<Int>()
    val averageTimeToTurnOff: LiveData<Int> get() = _averageTimeToTurnOff

    private val _maxTimeToTurnOff = MutableLiveData<Int>()
    val maxTimeToTurnOff: LiveData<Int> get() = _maxTimeToTurnOff

    private val _minTimeToTurnOff = MutableLiveData<Int>()
    val minTimeToTurnOff: LiveData<Int> get() = _minTimeToTurnOff

    private val _maxErrors = MutableLiveData<Int>()
    val maxErrors: LiveData<Int> get() = _maxErrors

    // Datos de ejemplo por rango de hora para cada tipo de gráfico
    private val _averageHourlyResponseTimeData = MutableLiveData<Map<String, Float>>()
    private val _maxHourlyResponseTimeData = MutableLiveData<Map<String, Float>>()
    private val _minHourlyResponseTimeData = MutableLiveData<Map<String, Float>>()
    private val _maxHourlyErrorsData = MutableLiveData<Map<String, Int>>()

    init {
        calculateGeneralStatistics()
        loadSampleHourlyData()
    }

    private fun calculateGeneralStatistics() {
        _averageTimeToTurnOff.value = 34  // Datos ficticios
        _maxTimeToTurnOff.value = 60
        _minTimeToTurnOff.value = 10
        _maxErrors.value = 4
    }

   private fun loadSampleHourlyData() {
        _averageHourlyResponseTimeData.value = mapOf(
            "5 - 8" to 6.5f,
            "9 - 11" to 7.3f,
            "12 - 14" to 5.8f,
            "15 - 19" to 8.2f,
            "20 - 24" to 6.9f,
            "1 - 4" to 4.3f
        )

        _maxHourlyResponseTimeData.value = mapOf(
            "5 - 8" to 15f,
            "9 - 11" to 14.5f,
            "12 - 14" to 12.3f,
            "15 - 19" to 16.8f,
            "20 - 24" to 13.2f,
            "1 - 4" to 10.4f
        )

        _minHourlyResponseTimeData.value = mapOf(
            "5 - 8" to 3f,
            "9 - 11" to 2f,
            "12 - 14" to 2.5f,
            "15 - 19" to 3.1f,
            "20 - 24" to 3.3f,
            "1 - 4" to 2.8f
        )

        _maxHourlyErrorsData.value = mapOf(
            "5 - 8" to 3,
            "9 - 11" to 2,
            "12 - 14" to 2,
            "15 - 19" to 3,
            "20 - 24" to 2,
            "1 - 4" to 1
        )
    }


    // Expose the appropriate LiveData based on the selected graph type
    fun getGraphData(type: String): LiveData<out Map<String, *>> {
        return when (type) {
            "Tiempo medio (seg)" -> _averageHourlyResponseTimeData
            "Tiempo máx. (seg)" -> _maxHourlyResponseTimeData
            "Tiempo mín. (seg)" -> _minHourlyResponseTimeData
            "Máx. errores" -> _maxHourlyErrorsData
            else -> _averageHourlyResponseTimeData
        }
    }
}
