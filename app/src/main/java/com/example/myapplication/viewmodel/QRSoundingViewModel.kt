package com.example.myapplication.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.model.AlarmStatistic
import java.text.SimpleDateFormat
import java.util.*

class QRSoundingViewModel(application: Application) : AndroidViewModel(application) {
    private val _alarmName = MutableLiveData<String>()
    val alarmName: LiveData<String> get() = _alarmName

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String> get() = _currentTime

    private val _shouldFinish = MutableLiveData<Boolean>()
    val shouldFinish: LiveData<Boolean> get() = _shouldFinish

     private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("alarm_statistics", Context.MODE_PRIVATE)
    private val statsRepository = AlarmStatsRepository(sharedPreferences)
    private var startTime: Long = System.currentTimeMillis()
    private var failures: Int = 0

    init {
        _currentTime.value = getCurrentTime()
        _shouldFinish.value = false
    }

    fun setAlarmName(name: String?) {
        _alarmName.value = name ?: ""
    }

    fun stopAlarm() {
        val currentTime = System.currentTimeMillis()
        val timeToTurnOff = currentTime - startTime

        // Crear una nueva estadística y guardarla
        val alarmStatistic = AlarmStatistic(
            id = System.currentTimeMillis().toString(),
            alarmSetTime = _currentTime.value ?: "Unknown",
            timeToTurnOff = timeToTurnOff,
            failures = failures
        )
        statsRepository.saveStatistic(alarmStatistic)

        // Indica a la actividad que debe cerrarse
        _shouldFinish.value = true
    }

    // Método para obtener la hora actual
    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}