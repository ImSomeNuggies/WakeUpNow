package com.example.myapplication

class AlarmRepository(private val alarmPreferences: AlarmPreferences) {

    fun getAlarms(): List<Alarm> {
        return alarmPreferences.loadAlarms()
    }

    fun saveAlarm(alarm: Alarm) {
        alarmPreferences.saveAlarm(alarm)
    }

    fun getNewAlarmId(): Int {
        return getAlarms().size + 1
    }
}
