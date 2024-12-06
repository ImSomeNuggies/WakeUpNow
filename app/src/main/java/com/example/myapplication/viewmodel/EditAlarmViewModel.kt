package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Alarm
import com.example.myapplication.repository.AlarmPreferences
import java.util.Calendar

class EditAlarmViewModel(private val alarmPreferences: AlarmPreferences) : ViewModel() {

    var alarm: Alarm? = null

    fun loadAlarm(alarmId: Int) {
        alarm = alarmPreferences.getAlarmById(alarmId)
    }

    fun updateAlarm(name: String, time: String, periodicity: String, problem: String) {
        alarm?.let {
            it.name = name
            it.time = time
            it.periodicity = periodicity
            it.problem = problem
            val timeParts = time.split(":").map { part -> part.toInt() }
            it.ringTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, timeParts[0])
                set(Calendar.MINUTE, timeParts[1])
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            alarmPreferences.editAlarm(it)
        }
    }

    fun deleteAlarm() {
        alarm?.let {
            alarmPreferences.deleteAlarm(it.id)
        }
    }
}
