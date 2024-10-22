package com.example.myapplication

/**
 * Repository class that acts as an abstraction layer between the ViewModel and SharedPreferences.
 * It interacts with the `AlarmPreferences` class to manage alarm data.
 * 
 * @param alarmPreferences The instance of `AlarmPreferences` used to save, load, and manage alarms.
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
     * Saves a new alarm using `AlarmPreferences`.
     *
     * @param alarm The `Alarm` object to be saved.
     */
    fun saveAlarm(alarm: Alarm) {
        alarmPreferences.saveAlarm(alarm)
    }

    /**
     * Generates a new alarm ID based on the current number of alarms.
     * 
     * @return A new unique alarm ID.
     */
    fun getNewAlarmId(): Int {
        return getAlarms().size + 1
    }
}
