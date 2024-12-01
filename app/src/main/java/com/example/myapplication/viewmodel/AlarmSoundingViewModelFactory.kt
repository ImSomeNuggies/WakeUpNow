package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlarmSoundingViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmSoundingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmSoundingViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
