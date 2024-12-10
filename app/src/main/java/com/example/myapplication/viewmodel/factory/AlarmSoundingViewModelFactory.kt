package com.example.myapplication.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.viewmodel.AlarmSoundingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlarmSoundingViewModelFactory(
    private val statsRepository: AlarmStatsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmSoundingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmSoundingViewModel(statsRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
