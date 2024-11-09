package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.viewmodels.AlarmSoundingViewModel
import com.example.myapplication.viewmodels.AlarmSoundingViewModelFactory
import com.example.myapplication.receivers.AlarmReceiver

class AlarmSoundingActivity : AppCompatActivity() {

    private lateinit var problemaTextView: TextView
    private lateinit var opcion1Button: Button
    private lateinit var opcion2Button: Button
    private lateinit var opcion3Button: Button
    private lateinit var opcion4Button: Button
    private lateinit var problemaFalloTextView: TextView
    private lateinit var textViewNombreAlarma: TextView
    private lateinit var textViewHoraActual: TextView

    // Usamos el ViewModel con un ViewModelFactory
    private val viewModel: AlarmSoundingViewModel by viewModels { AlarmSoundingViewModelFactory(application) }

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

        // Cargar y mostrar un problema aleatorio a través del ViewModel
        val problemas = AlarmSoundingViewModel.leerProblemasDesdeArchivo(application)
        val problemaAleatorio = viewModel.seleccionarProblemaAleatorio(problemas)

        problemaAleatorio?.let {
            problemaTextView.text = problemaAleatorio.problema
            opcion1Button.text = problemaAleatorio.opciones[0]
            opcion2Button.text = problemaAleatorio.opciones[1]
            opcion3Button.text = problemaAleatorio.opciones[2]
            opcion4Button.text = problemaAleatorio.opciones[3]

            // Asignar las acciones de los botones
            opcion1Button.setOnClickListener { viewModel.verificarRespuesta(problemaAleatorio.opciones[0], problemaAleatorio.correcta) }
            opcion2Button.setOnClickListener { viewModel.verificarRespuesta(problemaAleatorio.opciones[1], problemaAleatorio.correcta) }
            opcion3Button.setOnClickListener { viewModel.verificarRespuesta(problemaAleatorio.opciones[2], problemaAleatorio.correcta) }
            opcion4Button.setOnClickListener { viewModel.verificarRespuesta(problemaAleatorio.opciones[3], problemaAleatorio.correcta) }
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
