package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.viewmodels.QRSoundingViewModel
import com.example.myapplication.viewmodels.QRSoundingViewModelFactory
import com.example.myapplication.receivers.AlarmReceiver

class QRSoundingActivity : AppCompatActivity() {

    lateinit var problemaTextView: TextView
    lateinit var stopButton: Button
    lateinit var problemaFalloTextView: TextView
    private lateinit var textViewNombreAlarma: TextView
    private lateinit var textViewHoraActual: TextView

    // Usamos el ViewModel con un ViewModelFactory
    private val viewModel: QRSoundingViewModel by viewModels { QRSoundingViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_sounding)

        // Inicializar los elementos de la UI
        problemaTextView = findViewById(R.id.problemaTextView)
        stopButton = findViewById(R.id.stopButton)
        problemaFalloTextView = findViewById(R.id.problemaFallo)
        textViewNombreAlarma = findViewById(R.id.textViewNombreAlarma)
        textViewHoraActual = findViewById(R.id.textViewHoraActual)

        // Obtener el nombre de la alarma desde el Intent y asignarlo al TextView
        val alarmName = intent.getStringExtra("alarm_name")
        textViewNombreAlarma.text = alarmName ?: ""

        // Mostrar la hora actual
        val currentTime = viewModel.getCurrentTime()
        textViewHoraActual.text = currentTime

        // Observa el boton de stop
        stopButton.setOnClickListener { 
            viewModel.stopAlarm()
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
