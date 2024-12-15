package com.example.myapplication.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.viewmodel.SudokuSoundingViewModel

class SudokuSoundingViewModelFactory(
    private val statsRepository: AlarmStatsRepository,
    private val timeProvider: () -> Long = { System.currentTimeMillis() },
    private val dateFormatter: () -> String = {
        val dateFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        dateFormat.format(java.util.Date())
    }
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SudokuSoundingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SudokuSoundingViewModel(
                statsRepository = statsRepository,
                timeProvider = timeProvider,
                dateFormatter = dateFormatter
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}