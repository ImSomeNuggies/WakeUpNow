package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.AlarmStatsRepository
import java.math.BigDecimal
import java.math.RoundingMode

class StatisticsViewModel(private val repository: AlarmStatsRepository) : ViewModel() {

    // General statistics LiveData
    private val _averageTimeToTurnOff = MutableLiveData<Double>()
    val averageTimeToTurnOff: LiveData<Double> get() = _averageTimeToTurnOff

    private val _maxTimeToTurnOff = MutableLiveData<Double>()
    val maxTimeToTurnOff: LiveData<Double> get() = _maxTimeToTurnOff

    private val _minTimeToTurnOff = MutableLiveData<Double>()
    val minTimeToTurnOff: LiveData<Double> get() = _minTimeToTurnOff

    private val _maxErrors = MutableLiveData<Float>()
    val maxErrors: LiveData<Float> get() = _maxErrors

    // Hourly range data for each graph type
    private val _averageHourlyResponseTimeData = MutableLiveData<Map<String, Float>>()
    val averageHourlyResponseTimeData: LiveData<Map<String, Float>> get() = _averageHourlyResponseTimeData

    private val _maxHourlyResponseTimeData = MutableLiveData<Map<String, Float>>()
    val maxHourlyResponseTimeData: LiveData<Map<String, Float>> get() = _maxHourlyResponseTimeData

    private val _minHourlyResponseTimeData = MutableLiveData<Map<String, Float>>()
    val minHourlyResponseTimeData: LiveData<Map<String, Float>> get() = _minHourlyResponseTimeData

    private val _maxHourlyErrorsData = MutableLiveData<Map<String, Float>>()
    val maxHourlyErrorsData: LiveData<Map<String, Float>> get() = _maxHourlyErrorsData

    init {
        // Calculate and initialize general statistics and hourly data
        calculateGeneralStatistics()
        calculateHourlyData()
    }

    // Calculate overall statistics across all time ranges
    private fun calculateGeneralStatistics() {
        val startHour = 0
        val endHour = 23

        _averageTimeToTurnOff.value = roundTwoDecimalsDouble(repository.getAverageTimeInRange(startHour, endHour))
        _maxTimeToTurnOff.value = roundTwoDecimalsDouble(repository.getMaxTimeInRange(startHour, endHour))
        _minTimeToTurnOff.value = roundTwoDecimalsDouble(repository.getMinTimeInRange(startHour, endHour))
        _maxErrors.value = repository.getMaxErrorsInRange(startHour, endHour)
    }

    // Calculate statistics for predefined hourly ranges
    private fun calculateHourlyData() {
        val timeRanges = listOf(
            Pair(5, 8),
            Pair(9, 11),
            Pair(12, 14),
            Pair(15, 19),
            Pair(20, 23),
            Pair(0, 4)
        )

        // Maps to store statistics by time ranges
        val averageData = mutableMapOf<String, Float>()
        val maxData = mutableMapOf<String, Float>()
        val minData = mutableMapOf<String, Float>()
        val errorsData = mutableMapOf<String, Float>()

        for ((startHour, endHour) in timeRanges) {
            // Generate label for the time range
            val label = if (startHour == 0 && endHour == 4) {
                "0 - 4"
            } else {
                "$startHour - $endHour"
            }

            // Check if there are statistics for the current time range
            val hasData = repository.hasStatisticsInRange(startHour, endHour)

            if (hasData) {
                // Retrieve data from the repository
                val averageTime = repository.getAverageTimeInRange(startHour, endHour)
                val maxTime = repository.getMaxTimeInRange(startHour, endHour)
                val minTime = repository.getMinTimeInRange(startHour, endHour)
                val maxErrors = repository.getMaxErrorsInRange(startHour, endHour)

                // Add the data to the corresponding maps
                averageData[label] = roundTwoDecimalsFloat(averageTime)
                maxData[label] = roundTwoDecimalsFloat(maxTime)
                minData[label] = roundTwoDecimalsFloat(minTime)
                errorsData[label] = maxErrors
            }
        }

        // Update LiveData with calculated data
        _averageHourlyResponseTimeData.value = averageData
        _maxHourlyResponseTimeData.value = maxData
        _minHourlyResponseTimeData.value = minData
        _maxHourlyErrorsData.value = errorsData
    }

    // Expose the appropriate LiveData based on the selected graph type
    fun getGraphData(type: String): LiveData<out Map<String, Float>> {
        return when (type) {
            "Tiempo medio (seg)" -> _averageHourlyResponseTimeData
            "Tiempo máx. (seg)" -> _maxHourlyResponseTimeData
            "Tiempo mín. (seg)" -> _minHourlyResponseTimeData
            "Máx. errores" -> _maxHourlyErrorsData
            else -> _averageHourlyResponseTimeData
        }
    }

    // Round a Double value to two decimal places
    private fun roundTwoDecimalsDouble(value: Double): Double {
        return BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    // Round a Double value to two decimal places and convert to Float
    private fun roundTwoDecimalsFloat(value: Double): Float {
        return BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toFloat()
    }
}