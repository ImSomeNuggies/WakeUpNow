package com.example.myapplication.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.viewmodel.QRSoundingViewModel
import java.text.SimpleDateFormat
import java.util.*

class QRSoundingViewModelFactory(
    private val statsRepository: AlarmStatsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QRSoundingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QRSoundingViewModel(
                statsRepository = statsRepository,
                timeProvider = { System.currentTimeMillis() },
                dateFormatter = {
                    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    dateFormat.format(Date())
                }
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}