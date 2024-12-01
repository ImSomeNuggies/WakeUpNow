package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.model.AlarmStatistic
import com.example.myapplication.model.Sudoku
import java.text.SimpleDateFormat
import java.util.*

class SudokuSoundingViewModel(application: Application) : AndroidViewModel(application) {
    private val _alarmName = MutableLiveData<String>()
    val alarmName: LiveData<String> get() = _alarmName

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String> get() = _currentTime

    private val _shouldFinish = MutableLiveData<Boolean>()
    val shouldFinish: LiveData<Boolean> get() = _shouldFinish

    private val statsRepository = AlarmStatsRepository(application)
    private var startTime: Long = System.currentTimeMillis()
    private var failures: Int = 0

    // Instancia del Sudoku para manejar la lógica del tablero
    private val sudoku = Sudoku()

    init {
        _currentTime.value = getCurrentTime()
        _shouldFinish.value = false
        sudoku.initializeSudoku()
    }

    fun setAlarmName(name: String?) {
        _alarmName.value = name ?: ""
    }

    // Método para obtener la hora actual
    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    // Verifica si un número puede colocarse en la celda y lo coloca si es correcto
    fun checkAndPlaceNumber(row: Int, col: Int, selectedNumber: Int): Boolean {
        val isCorrect = sudoku.placeNumber(row, col, selectedNumber)
        if (!isCorrect) {
            failures++
        } else {
            if(sudoku.isSudokuCompleted()) {
                stopAlarm()
            }
        }
        return isCorrect
    }

    // Verifica si una celda es editable
    fun isEditable(row: Int, col: Int): Boolean {
        return sudoku.isEditable(row, col)
    }

    // Devuelve el tablero actual del usuario
    fun getBoard(): Array<IntArray> {
        return sudoku.getUserBoard()
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
}