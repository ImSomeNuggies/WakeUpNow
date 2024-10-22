package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.myapplication.receivers.AlarmReceiver



class AlarmSounding : AppCompatActivity() {

    lateinit var problemaTextView: TextView
    lateinit var opcion1Button: Button
    lateinit var opcion2Button: Button
    lateinit var opcion3Button: Button
    lateinit var opcion4Button: Button
    lateinit var problemaFalloTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_sounding)

        //Inicializar los elementos de la UI
        problemaTextView = findViewById(R.id.problemaTextView)
        opcion1Button = findViewById(R.id.opcion1Button)
        opcion2Button = findViewById(R.id.opcion2Button)
        opcion3Button = findViewById(R.id.opcion3Button)
        opcion4Button = findViewById(R.id.opcion4Button)
        problemaFalloTextView = findViewById(R.id.problemaFallo)

        //Cargar y mostrar un problema aleatorio
        val problemas = leerProblemasDesdeArchivo()
        val problemaAleatorio = seleccionarProblemaAleatorio(problemas)

        problemaAleatorio?.let {
            problemaTextView.text = problemaAleatorio.problema
            opcion1Button.text = problemaAleatorio.opciones[0]
            opcion2Button.text = problemaAleatorio.opciones[1]
            opcion3Button.text = problemaAleatorio.opciones[2]
            opcion4Button.text = problemaAleatorio.opciones[3]

            //Asignar las acciones de los botones
            opcion1Button.setOnClickListener { verificarRespuesta(problemaAleatorio.opciones[0], problemaAleatorio.correcta)}
            opcion2Button.setOnClickListener { verificarRespuesta(problemaAleatorio.opciones[1], problemaAleatorio.correcta)}
            opcion3Button.setOnClickListener { verificarRespuesta(problemaAleatorio.opciones[2], problemaAleatorio.correcta)}
            opcion4Button.setOnClickListener { verificarRespuesta(problemaAleatorio.opciones[3], problemaAleatorio.correcta)}
        }
    }

    //Leer problemas desde el archivo JSON en la carpeta assets
    fun leerProblemasDesdeArchivo(): List<Problema> {
        val jsonString = assets.open("problemas.json")
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
            //Muestra que la respuesta es correcta y termina la actividad
            AlarmReceiver.ringtone?.let {
                if (it.isPlaying) {
                    it.stop()
                    AlarmReceiver.ringtone = null
                }
            }
            finish()
        } else {
            //Muestra que la respuesta es incorrecta, tal vez un mensaje de error
            problemaFalloTextView.text = "Respuesta incorrecta. Inténtalo de nuevo."

        }
    }
}