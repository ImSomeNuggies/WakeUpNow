package com.example.myapplication.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.AlarmPreferences
import com.example.myapplication.viewmodel.EditAlarmViewModel

class EditAlarmViewModelFactory(private val alarmPreferences: AlarmPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditAlarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditAlarmViewModel(alarmPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}