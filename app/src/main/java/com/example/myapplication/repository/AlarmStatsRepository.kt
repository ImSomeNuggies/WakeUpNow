package com.example.myapplication.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.model.AlarmStatistic
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class AlarmStatsRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AlarmStats", Context.MODE_PRIVATE)

    private val gson = Gson()

    // Saves a new alarm statistic to the shared preferences
    fun saveStatistic(alarmStatistic: AlarmStatistic) {
        val stats = getAllStatistics().toMutableList()

        stats.add(alarmStatistic)

        // Serializes the updated statistics list into JSON format and stores it
        val json = gson.toJson(stats)
        sharedPreferences.edit().putString("statistics", json).apply()
    }

    // Retrieves all saved alarm statistics as a list
    fun getAllStatistics(): List<AlarmStatistic> {
        val json = sharedPreferences.getString("statistics", null) ?: return emptyList()
        val type = object : TypeToken<List<AlarmStatistic>>() {}.type
        return gson.fromJson(json, type)
    }

    // Checks if there are any statistics within a specific hour range
    fun hasStatisticsInRange(startHour: Int, endHour: Int): Boolean {
        return getStatisticsInRange(startHour, endHour).isNotEmpty()
    }

    // Calculates the average time to turn off the alarm within a specific hour range
    fun getAverageTimeInRange(startHour: Int, endHour: Int): Double {
        val timesInRange = getTimesToTurnOffInRange(startHour, endHour)
        return if (timesInRange.isNotEmpty()) {
            timesInRange.average()
        } else {
            0.0
        }
    }

    // Retrieves the minimum time to turn off the alarm within a specific hour range
    fun getMinTimeInRange(startHour: Int, endHour: Int): Double {
        val timesInRange = getTimesToTurnOffInRange(startHour, endHour)
        return if (timesInRange.isNotEmpty()) {
            timesInRange.minOrNull() ?: 0.0
        } else {
            0.0
        }
    }

    // Retrieves the maximum time to turn off the alarm within a specific hour range
    fun getMaxTimeInRange(startHour: Int, endHour: Int): Double {
        val timesInRange = getTimesToTurnOffInRange(startHour, endHour)
        return if (timesInRange.isNotEmpty()) {
            timesInRange.maxOrNull() ?: 0.0
        } else {
            0.0
        }
    }

    // Retrieves the maximum number of failures within a specific hour range
    fun getMaxErrorsInRange(startHour: Int, endHour: Int): Float {
        val intList = getFailuresInRange(startHour, endHour);
        val errorsInRange: List<Float> = intList.map { it.toFloat() }
        return if (errorsInRange.isNotEmpty()) {
            errorsInRange.maxOrNull() ?: 0.0f
        } else {
            0.0f
        }
    }

    // Helper function to get all turn-off times (in seconds) within a specific hour range
    private fun getTimesToTurnOffInRange(startHour: Int, endHour: Int): List<Double> {
        val statisticsInRange = getStatisticsInRange(startHour, endHour)
        return statisticsInRange.map { it.timeToTurnOff / 1000.0 } // Converts milliseconds to seconds
    }

    // Helper function to get all failure counts within a specific hour range
    private fun getFailuresInRange(startHour: Int, endHour: Int): List<Int> {
        val statisticsInRange = getStatisticsInRange(startHour, endHour)
        return statisticsInRange.map { it.failures }
    }

    // Helper function to filter statistics based on a specific hour range
    private fun getStatisticsInRange(startHour: Int, endHour: Int): List<AlarmStatistic> {
        val statistics = getAllStatistics()
        if (statistics.isEmpty()) return emptyList()

        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

        return statistics.filter { stat ->
            try {
                val date = formatter.parse(stat.alarmSetTime)
                val calendar = Calendar.getInstance()

                if (date != null) {
                    calendar.time = date
                }else{
                    throw IllegalArgumentException("La fecha no puede ser nula")
                }

                val hour = calendar.get(Calendar.HOUR_OF_DAY)

                if (startHour <= endHour) {
                    // Normal range, e.g., from 5 to 8
                    hour in startHour..endHour
                } else {
                    // Range that crosses midnight, e.g., from 22 to 2
                    hour >= startHour || hour <= endHour
                }
            } catch (e: Exception) {
                // If there's an error parsing the time, exclude this record
                false
            }
        }
    }

}