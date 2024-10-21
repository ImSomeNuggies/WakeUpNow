package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.AlarmPreferences

class EditAlarmViewModelFactory(private val alarmPreferences: AlarmPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditAlarmViewModel::class.java)) {
            return EditAlarmViewModel(alarmPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}