package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.model.AlarmStatistic
import java.text.SimpleDateFormat
import java.util.*

class QRSoundingViewModel(
    private val statsRepository: AlarmStatsRepository,
    private val timeProvider: () -> Long = { System.currentTimeMillis() },
    private val dateFormatter: () -> String = {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        dateFormat.format(Date())
    }
) : ViewModel() {

    private val _alarmName = MutableLiveData<String>()
    val alarmName: LiveData<String> get() = _alarmName

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String> get() = _currentTime

    private val _shouldFinish = MutableLiveData<Boolean>()
    val shouldFinish: LiveData<Boolean> get() = _shouldFinish

    private var startTime: Long = timeProvider()
    private var failures: Int = 0

    init {
        _currentTime.value = getCurrentTime()
        _shouldFinish.value = false
    }

    fun setAlarmName(name: String?) {
        _alarmName.value = name ?: ""
    }

    fun stopAlarm() {
        val currentTime = timeProvider()
        val timeToTurnOff = currentTime - startTime

        // Crear una nueva estadística y guardarla
        val alarmStatistic = AlarmStatistic(
            id = timeProvider().toString(),
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
        return dateFormatter()
    }

    // Método para obtener el valor de failures (solo para tests)
    fun getShouldFinish(): Boolean? {
        return _shouldFinish.value
    }
    fun setShouldFinishForTesting(shouldFinish : Boolean) {
        _shouldFinish.value = shouldFinish
    }

}