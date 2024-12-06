package com.example.myapplication.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.viewmodel.QrGeneratorViewModel

class QrGeneratorViewModelFactory (private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QrGeneratorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QrGeneratorViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}