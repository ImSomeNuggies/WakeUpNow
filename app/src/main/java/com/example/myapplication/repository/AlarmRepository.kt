package com.example.myapplication.repository

import com.example.myapplication.model.Alarm


/**
 * Repository class that acts as an abstraction layer between the ViewModel and SharedPreferences.
 * It interacts with the `AlarmPreferences` class to manage alarm data.
 *
 * @param alarmPreferences The `AlarmPreferences` instance used to manage alarm data.
 */
class AlarmRepository(private val alarmPreferences: AlarmPreferences) {

    /**
     * Retrieves all alarms from SharedPreferences via `AlarmPreferences`.
     *
     * @return A list of `Alarm` objects.
     */
    fun getAlarms(): List<Alarm> {
        return alarmPreferences.loadAlarms()
    }

    /**
     * Retrieves an alarm by its ID from SharedPreferences.
     *
     * @param alarmId The ID of the alarm to retrieve.
     * @return The `Alarm` object if found, or null if not found.
     */
    fun getAlarmById(alarmId: Int): Alarm? {
        return alarmPreferences.getAlarmById(alarmId)
    }


    /**
     * Saves a new alarm using `AlarmPreferences`.
     *
     * @param alarm The `Alarm` object to be saved.
     */
    fun saveAlarm(alarm: Alarm) {
        alarmPreferences.saveAlarm(alarm)
    }

    /**
     * Edits an existing alarm by updating its data in SharedPreferences.
     *
     * @param alarm The `Alarm` object with updated information.
     */
    fun editAlarm(alarm: Alarm) {
        alarmPreferences.editAlarm(alarm)
    }

    /**
     * Deletes an alarm from SharedPreferences by its ID.
     *
     * @param alarmId The ID of the alarm to delete.
     */
    fun deleteAlarm(alarmId: Int) {
        alarmPreferences.deleteAlarm(alarmId)
    }

    /**
    * Generates a new unique alarm ID.
    * This method calculates the new ID by finding the maximum ID among the existing alarms
    * and adding 1. If there are no alarms, it starts the IDs from 1.
    *
    * @return A new unique alarm ID, sequentially incremented based on existing alarms.
    */
    fun getNewAlarmId(): Int {
        val alarms = getAlarms()
        return (alarms.maxOfOrNull { it.id } ?: 0) + 1
    }
}
