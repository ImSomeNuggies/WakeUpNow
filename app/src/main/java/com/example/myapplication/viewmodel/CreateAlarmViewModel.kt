package com.example.myapplication.viewmodel


import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Alarm
import com.example.myapplication.repository.AlarmRepository
import java.util.*

class CreateAlarmViewModel(private val repository: AlarmRepository) : ViewModel() {

    var selectedTime: String = ""
    var selectedPeriodicity: String = ""
    var selectedProblem: String = ""


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
            problem = selectedProblem,
            isActive = true,
            ringTime = newAlarmCalendar
        )



        repository.saveAlarm(newAlarm)
        return newAlarm
    }

}
