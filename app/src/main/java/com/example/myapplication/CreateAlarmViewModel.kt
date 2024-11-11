package com.example.myapplication


import androidx.lifecycle.ViewModel
import java.util.*

class CreateAlarmViewModel(private val repository: AlarmRepository) : ViewModel() {

    var selectedTime: String = ""
    var selectedPeriodicity: String = ""


    /*fun setSelectedTime(time: String) {
        selectedTime = time
    }*/

    fun saveAlarm(name: String): Alarm {
        val timeParts = selectedTime.split(":").map { it.toInt() }
        val hour = timeParts[0]
        val minute = timeParts[1]

        val newAlarmCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val newAlarm = Alarm(
            id = repository.getNewAlarmId(),
            time = selectedTime,
            name = name,
            periodicity = selectedPeriodicity,
            isActive = true,
            ringTime = newAlarmCalendar
        )



        repository.saveAlarm(newAlarm)
        return newAlarm
    }

    fun whichDay(day: String){

    }
}
