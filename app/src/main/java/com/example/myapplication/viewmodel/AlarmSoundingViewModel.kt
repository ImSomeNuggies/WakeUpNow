package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Problema
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.model.AlarmStatistic
import kotlin.random.Random
import java.text.SimpleDateFormat
import java.util.*

class AlarmSoundingViewModel(
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

    private val _problema = MutableLiveData<Problema>()
    val problema: LiveData<Problema> get() = _problema

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _shouldFinish = MutableLiveData<Boolean>()
    val shouldFinish: LiveData<Boolean> get() = _shouldFinish

    private var startTime: Long = timeProvider()
    private var failures: Int = 0

    init {
        _currentTime.value = getCurrentTime()
        _shouldFinish.value = false
        _problema.value = crearProblemaAleatorio()
    }

    fun setAlarmName(name: String?) {
        _alarmName.value = name ?: ""
    }

    fun getCurrentTime(): String {
        return dateFormatter()
    }

    fun verificarRespuesta(respuestaSeleccionada: String, respuestaCorrecta: String) {
        if (respuestaSeleccionada == respuestaCorrecta) {
            val currentTime = timeProvider()
            val timeToTurnOff = currentTime - startTime

            val alarmStatistic = AlarmStatistic(
                id = timeProvider().toString(),
                alarmSetTime = _currentTime.value ?: "Unknown",
                timeToTurnOff = timeToTurnOff,
                failures = failures
            )
            statsRepository.saveStatistic(alarmStatistic)

            _shouldFinish.value = true
        } else {
            failures++
            _errorMessage.value = "Respuesta incorrecta. Inténtalo de nuevo."
            _problema.value = crearProblemaAleatorio()
        }
    }

    fun crearProblemaAleatorio(): Problema {
        val random = Random(timeProvider())
        return Problema("Enunciado", listOf("Opción 1", "Opción 2", "Opción 3"), "Opción correcta")
            .crearProblemaAleatorio(random = random)
    }

    // Métodos auxiliares para pruebas
    fun setProblemaForTesting(problema: Problema) {
        _problema.value = problema
    }

    fun setShouldFinishForTesting(value: Boolean) {
        _shouldFinish.value = value
    }

    fun getFailures(): Int {
        return failures
    }

    fun setStartTimeForTesting(startTime: Long) {
        this.startTime = startTime
    }
}
