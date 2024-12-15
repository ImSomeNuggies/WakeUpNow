package com.example.myapplication.model

import com.google.gson.Gson

data class AlarmStatistic(
    val id: String,           // Unique ID for the statistic
    val alarmSetTime: String, // Time the alarm was set to ring
    val timeToTurnOff: Long,  // Time taken to turn off the alarm (in milliseconds)
    val failures: Int         // Number of failures
) {
    companion object {
        private val gson = Gson()

        // Convert a JSON string to an AlarmStatistic object
        fun fromJson(json: String): AlarmStatistic {
            return gson.fromJson(json, AlarmStatistic::class.java)
        }

        // Convert an AlarmStatistic object to a JSON string
        fun toJson(statistic: AlarmStatistic): String {
            return gson.toJson(statistic)
        }
    }
}