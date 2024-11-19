package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Problema
import com.example.myapplication.AlarmSoundingActivity
import com.example.myapplication.receivers.AlarmReceiver
import java.text.SimpleDateFormat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class AlarmSoundingViewModel(application: Application) : AndroidViewModel(application) {

    private val _alarmName = MutableLiveData<String>()
    val alarmName: LiveData<String> get() = _alarmName

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String> get() = _currentTime

    private val _problema = MutableLiveData<Problema>()
    val problema: LiveData<Problema> get() = _problema

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _shouldFinish = MutableLiveData<Boolean>()
    val shouldFinish: LiveData<Boolean> get() = _shouldFinish

    // Lista de problemas cargada desde el archivo
    private val problemas: List<Problema> = leerProblemasDesdeArchivo()
    
    init {
        _currentTime.value = getCurrentTime()
        _shouldFinish.value = false
        seleccionarProblemaAleatorio(problemas)
    }

    fun setAlarmName(name: String?) {
        _alarmName.value = name ?: ""
    }

    //Leer problemas desde el archivo JSON en la carpeta assets
    fun leerProblemasDesdeArchivo(): List<Problema> {
        val jsonString = getApplication<Application>().assets.open("problemas.json")
            .bufferedReader()
            .use { it.readText() }

        val listType = object : TypeToken<List<Problema>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    //Seleccionar un problema al azar
    fun seleccionarProblemaAleatorio(problemas: List<Problema>): Problema? {
        return if (problemas.isNotEmpty()) {
            problemas.random()
        } else {
            null
        }
    }

    //Verificar si la respuesta seleccionada es correcta
    fun verificarRespuesta(respuestaSeleccionada: String, respuestaCorrecta: String) {
        if (respuestaSeleccionada == respuestaCorrecta) {
            // Indica a la actividad que debe cerrarse
            _shouldFinish.value = true
        } else {
            _errorMessage.value = "Respuesta incorrecta. Inténtalo de nuevo."
        }
        
    }

    // Método para obtener la hora actual
    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}
