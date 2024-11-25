package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SudokuSoundingViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SudokuSoundingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SudokuSoundingViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
