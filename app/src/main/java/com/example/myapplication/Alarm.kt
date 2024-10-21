package com.example.myapplication

import java.util.Calendar

/**
 * Data class representing an alarm entity in the application.
 * 
 * This class holds all necessary information for an alarm:
 * - id: Unique identifier for each alarm.
 * - time: The string representing the time of the alarm.
 * - name: The name of the alarm.
 * - periodicity: The frequency or repetition schedule of the alarm (e.g., "Daily", "Weekly").
 * - isActive: A boolean indicating whether the alarm is currently active or inactive.
 * - ringTime: A Calendar object that stores the actual time the alarm is set to ring in the system.
 */
data class Alarm(
    val id: Int,                  // Unique ID for the alarm
    var time: String,             // Time in HH:mm format
    var name: String,             // Alarm label/name
    var periodicity: String,      // Repetition pattern (e.g., Daily, Weekly)
    var isActive: Boolean,        // Flag indicating if the alarm is active
    var ringTime: Calendar        // Calendar object representing the ring time in the system
)
