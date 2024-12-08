package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.model.AlarmStatistic
import com.example.myapplication.model.Sudoku
import java.text.SimpleDateFormat
import java.util.*

class SudokuSoundingViewModel(
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
    private val _failures = MutableLiveData<Int>()

    // Instancia del Sudoku para manejar la lógica del tablero
    private val sudoku = Sudoku()

    init {
        _currentTime.value = getCurrentTime()
        _shouldFinish.value = false
        _failures.value = 0
        sudoku.initializeSudoku()
    }

    fun setAlarmName(name: String?) {
        _alarmName.value = name ?: ""
    }

    // Método para obtener la hora actual
    fun getCurrentTime(): String {
        return dateFormatter()
    }

    // Verifica si un número puede colocarse en la celda y lo coloca si es correcto
    fun checkAndPlaceNumber(row: Int, col: Int, selectedNumber: Int): Boolean {
        val isCorrect = sudoku.placeNumber(row, col, selectedNumber)
        if (!isCorrect) {
            _failures.value = (_failures.value ?: 0) + 1
        } else if(sudoku.isSudokuCompleted()) {
            stopAlarm()
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
        val currentTime = timeProvider()
        val timeToTurnOff = currentTime - startTime

        // Crear una nueva estadística y guardarla
        val alarmStatistic = AlarmStatistic(
            id = timeProvider().toString(),
            alarmSetTime = _currentTime.value ?: "Unknown",
            timeToTurnOff = timeToTurnOff,
            failures = _failures.value ?: 0
        )
        statsRepository.saveStatistic(alarmStatistic)

        // Indica a la actividad que debe cerrarse
        _shouldFinish.value = true
    }

    // Método para obtener el valor de failures (solo para tests)
    fun getFailures(): Int? {
        return _failures.value
    }

    // Método para obtener el valor de failures (solo para tests)
    fun getShouldFinish(): Boolean? {
        return _shouldFinish.value
    }

    // Método para obtener la solución del Sudoku
    fun getSolution(): Array<IntArray> {
        return sudoku.getSolution()
    }

}