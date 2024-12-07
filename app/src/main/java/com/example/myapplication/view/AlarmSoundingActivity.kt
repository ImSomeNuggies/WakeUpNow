package com.example.myapplication.view

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.viewmodel.AlarmSoundingViewModel
import com.example.myapplication.viewmodel.factory.AlarmSoundingViewModelFactory
import com.example.myapplication.viewmodel.AlarmReceiver

class AlarmSoundingActivity : AppCompatActivity() {

    lateinit var problemaTextView: TextView
    lateinit var opcion1Button: Button
    lateinit var opcion2Button: Button
    lateinit var opcion3Button: Button
    lateinit var opcion4Button: Button
    lateinit var problemaFalloTextView: TextView
    private lateinit var textViewNombreAlarma: TextView
    private lateinit var textViewHoraActual: TextView

    // Usamos el ViewModel con un ViewModelFactory
    private val viewModel: AlarmSoundingViewModel by viewModels {
        val sharedPreferences = getSharedPreferences("alarm_statistics", Context.MODE_PRIVATE)
        val statsRepository = AlarmStatsRepository(sharedPreferences)
        AlarmSoundingViewModelFactory(statsRepository) // Pasamos el repositorio aquí
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_sounding)

        // Inicializar los elementos de la UI
        problemaTextView = findViewById(R.id.problemaTextView)
        opcion1Button = findViewById(R.id.opcion1Button)
        opcion2Button = findViewById(R.id.opcion2Button)
        opcion3Button = findViewById(R.id.opcion3Button)
        opcion4Button = findViewById(R.id.opcion4Button)
        problemaFalloTextView = findViewById(R.id.problemaFallo)
        textViewNombreAlarma = findViewById(R.id.textViewNombreAlarma)
        textViewHoraActual = findViewById(R.id.textViewHoraActual)

        // Obtener el nombre de la alarma desde el Intent y asignarlo al TextView
        val alarmName = intent.getStringExtra("alarm_name")
        textViewNombreAlarma.text = alarmName ?: ""

        // Mostrar la hora actual
        val currentTime = viewModel.getCurrentTime()
        textViewHoraActual.text = currentTime

        // Observa el problema actual y actualiza la UI
        viewModel.problema.observe(this) { problema ->
            problema?.let {
                problemaTextView.text = it.enunciado
                opcion1Button.text = it.opciones[0]
                opcion2Button.text = it.opciones[1]
                opcion3Button.text = it.opciones[2]
                opcion4Button.text = it.opciones[3]
            }
        }

        // Asignar las acciones de los botones
        opcion1Button.setOnClickListener {
            val problemaActual = viewModel.problema.value
            if (problemaActual != null) {
                viewModel.verificarRespuesta(problemaActual.opciones[0], problemaActual.respuestaCorrecta)
            }
        }
        opcion2Button.setOnClickListener {
            val problemaActual = viewModel.problema.value
            if (problemaActual != null) {
                viewModel.verificarRespuesta(problemaActual.opciones[1], problemaActual.respuestaCorrecta)
            }
        }
        opcion3Button.setOnClickListener {
            val problemaActual = viewModel.problema.value
            if (problemaActual != null) {
                viewModel.verificarRespuesta(problemaActual.opciones[2], problemaActual.respuestaCorrecta)
            }
        }
        opcion4Button.setOnClickListener {
            val problemaActual = viewModel.problema.value
            if (problemaActual != null) {
                viewModel.verificarRespuesta(problemaActual.opciones[3], problemaActual.respuestaCorrecta)
            }
        }

        // Observa shouldFinish para detener la alarma y cerrar la actividad
        viewModel.shouldFinish.observe(this) { shouldFinish ->
            if (shouldFinish == true) {
                AlarmReceiver.stopAlarm()  // Detiene la alarma antes de cerrar la actividad
                finish()  // Cierra la actividad cuando shouldFinish es true
            }
        }
    }
}
