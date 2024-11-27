package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Problema
import com.example.myapplication.repositories.AlarmStatsRepository
import com.example.myapplication.models.AlarmStatistic
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.random.Random
import kotlin.math.*
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

    private var sudokuBoard = Array(4) { IntArray(4) { 0 } }
    private val _currentBoard = MutableLiveData<Array<IntArray>>()
    val currentBoard: LiveData<Array<IntArray>> get() = _currentBoard

    init {
        _currentTime.value = getCurrentTime()
        _shouldFinish.value = false
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

    fun initializeSudoku() {
        sudokuBoard = Array(4) { IntArray(4) { 0 } }
        fillSudoku()
        removeRandomCells()
        _currentBoard.value = sudokuBoard
    }

    private fun fillSudoku() {
        val nums = (1..4).toList()
        for (i in 0 until 4) {
            var shuffled = nums.shuffled()
            for (j in 0 until 4) {
                while (!isValidPlacement(i, j, shuffled.first())) {
                    shuffled = nums.shuffled()
                }
                sudokuBoard[i][j] = shuffled.first()
            }
        }
    }

    private fun removeRandomCells() {
        val cellsToRemove = 6 // Puedes ajustar la dificultad
        repeat(cellsToRemove) {
            val row = Random.nextInt(0, 4)
            val col = Random.nextInt(0, 4)
            sudokuBoard[row][col] = 0
        }
    }

    private fun isValidPlacement(row: Int, col: Int, num: Int): Boolean {
        // Revisa fila y columna
        if (sudokuBoard[row].contains(num)) return false
        if (sudokuBoard.any { it[col] == num }) return false

        // Revisa subcuadrícula
        val subGridRowStart = (row / 2) * 2
        val subGridColStart = (col / 2) * 2
        for (r in subGridRowStart until subGridRowStart + 2) {
            for (c in subGridColStart until subGridColStart + 2) {
                if (sudokuBoard[r][c] == num) return false
            }
        }
        return true
    }

    fun checkNumber(row: Int, col: Int, num: Int): Boolean {
        return sudokuBoard[row][col] == num
    }

    fun validateAndUpdateCompletion(userBoard: Array<IntArray>) {
        if (isSudokuSolved(userBoard)) {
            _shouldFinish.value = true
        }
    }

    private fun isSudokuSolved(userBoard: Array<IntArray>): Boolean {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (userBoard[i][j] != sudokuBoard[i][j]) return false
            }
        }
        return true
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