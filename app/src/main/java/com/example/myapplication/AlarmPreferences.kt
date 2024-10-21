package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.Alarm
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Class responsible for managing alarms in SharedPreferences.
 * Provides methods to save, load, edit, and delete alarms using JSON serialization with Gson.
 */
class AlarmPreferences(context: Context) {
    // SharedPreferences instance to store and retrieve alarms
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("alarms", Context.MODE_PRIVATE)
    
    // Gson instance for JSON serialization/deserialization
    private val gson = Gson()
    
    /**
     * Retrieves an alarm by its ID from SharedPreferences.
     *
     * @param alarmId The ID of the alarm to retrieve.
     * @return The Alarm object if found, or null if not.
     */
    fun getAlarmById(alarmId: Int): Alarm? {
        // Retrieve the list of alarms as a JSON string from SharedPreferences
        val alarmsJson = sharedPreferences.getString("alarms_list", null)
        val alarmListType = object : TypeToken<MutableList<Alarm>>() {}.type
        val alarmList: MutableList<Alarm> = if (alarmsJson != null) {
            // Deserialize the JSON string into a list of Alarm objects
            gson.fromJson(alarmsJson, alarmListType) ?: mutableListOf()
        } else {
            mutableListOf()
        }

        // Find and return the alarm with the matching ID, or null if not found
        return alarmList.find { it.id == alarmId }
    }

    /**
     * Saves a new alarm to SharedPreferences.
     * 
     * @param alarm The Alarm object to be saved.
     */
    fun saveAlarm(alarm: Alarm) {
        val editor = sharedPreferences.edit()

        // Retrieve the existing list of alarms from SharedPreferences
        val existingAlarmsJson = sharedPreferences.getString("alarms_list", null)
        val alarmListType = object : TypeToken<MutableList<Alarm>>() {}.type
        val existingAlarms: MutableList<Alarm> = if (existingAlarmsJson != null) {
            // Deserialize the existing list of alarms, or create an empty list if null
            gson.fromJson(existingAlarmsJson, alarmListType) ?: mutableListOf()
        } else {
            mutableListOf()
        }

        // Add the new alarm to the list
        existingAlarms.add(alarm)

        // Serialize the updated list back to JSON and save it in SharedPreferences
        val newAlarmsJson = gson.toJson(existingAlarms)
        editor.putString("alarms_list", newAlarmsJson)
        editor.apply()
    }

    /**
     * Edits an existing alarm in SharedPreferences.
     *
     * @param editedAlarm The Alarm object with updated information.
     */
    fun editAlarm(editedAlarm: Alarm) {
        val editor = sharedPreferences.edit()

        // Retrieve the existing list of alarms from SharedPreferences
        val existingAlarmsJson = sharedPreferences.getString("alarms_list", null)
        val alarmListType = object : TypeToken<MutableList<Alarm>>() {}.type
        val existingAlarms: MutableList<Alarm> = if (existingAlarmsJson != null) {
            gson.fromJson(existingAlarmsJson, alarmListType) ?: mutableListOf()
        } else {
            mutableListOf()
        }

        // Find the alarm by its ID and update it
        val alarmIndex = existingAlarms.indexOfFirst { it.id == editedAlarm.id }
        if (alarmIndex != -1) {
            existingAlarms[alarmIndex] = editedAlarm
        }

        // Serialize the updated list of alarms and save it back to SharedPreferences
        val newAlarmsJson = gson.toJson(existingAlarms)
        editor.putString("alarms_list", newAlarmsJson)
        editor.apply()
    }

    /**
     * Deletes an alarm from SharedPreferences by its ID.
     *
     * @param alarmId The ID of the alarm to delete.
     */
    fun deleteAlarm(alarmId: Int) {
        val editor = sharedPreferences.edit()

        // Retrieve the existing list of alarms from SharedPreferences
        val existingAlarmsJson = sharedPreferences.getString("alarms_list", null)
        val alarmListType = object : TypeToken<MutableList<Alarm>>() {}.type
        val existingAlarms: MutableList<Alarm> = if (existingAlarmsJson != null) {
            gson.fromJson(existingAlarmsJson, alarmListType) ?: mutableListOf()
        } else {
            mutableListOf()
        }

        // Find and remove the alarm by its ID
        val alarmToRemove = existingAlarms.find { it.id == alarmId }
        if (alarmToRemove != null) {
            existingAlarms.remove(alarmToRemove)
        }

        // Serialize the updated list and save it back to SharedPreferences
        val newAlarmsJson = gson.toJson(existingAlarms)
        editor.putString("alarms_list", newAlarmsJson)
        editor.apply()
    }

    /**
     * Loads all alarms from SharedPreferences.
     *
     * @return A MutableList of Alarm objects.
     */
    fun loadAlarms(): MutableList<Alarm> {
        // Retrieve the list of alarms as a JSON string from SharedPreferences
        val existingAlarmsJson = sharedPreferences.getString("alarms_list", null)
        val alarmListType = object : TypeToken<MutableList<Alarm>>() {}.type

        // Deserialize the JSON string into a list of Alarm objects, or return an empty list if null
        return if (existingAlarmsJson != null) {
            gson.fromJson(existingAlarmsJson, alarmListType) ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }
}
