package com.example.myapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.Alarm
import com.example.myapplication.AlarmPreferences
import java.util.Calendar

class EditAlarmViewModel(private val alarmPreferences: AlarmPreferences) : ViewModel() {

    var alarm: Alarm? = null

    fun loadAlarm(alarmId: Int) {
        alarm = alarmPreferences.getAlarmById(alarmId)
    }

    fun updateAlarm(name: String, time: String, periodicity: String) {
        alarm?.let {
            it.name = name
            it.time = time
            it.periodicity = periodicity
            val timeParts = time.split(":").map { part -> part.toInt() }
            it.ringTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, timeParts[0])
                set(Calendar.MINUTE, timeParts[1])
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            alarmPreferences.editAlarm(it)
            Log.d("AlarmUpdate", "Id: ${it.id}, Name: ${it.name}, Hour: ${it.time}, Periodicity: ${it.periodicity}")

        }
    }

    fun deleteAlarm() {
        alarm?.let {
            alarmPreferences.deleteAlarm(it.id)
        }
    }
}
