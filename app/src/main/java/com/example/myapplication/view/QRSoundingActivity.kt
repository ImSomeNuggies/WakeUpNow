package com.example.myapplication.view

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.viewmodel.QRSoundingViewModel
import com.example.myapplication.viewmodel.factory.QRSoundingViewModelFactory
import com.example.myapplication.viewmodel.AlarmReceiver
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class QRSoundingActivity : AppCompatActivity() {

    private lateinit var stopButton: Button
    private lateinit var textViewNombreAlarma: TextView
    private lateinit var textViewHoraActual: TextView

    // Crear el repositorio y ViewModelFactory
    val viewModel: QRSoundingViewModel by viewModels {
        val sharedPreferences = getSharedPreferences("alarm_statistics", Context.MODE_PRIVATE)
        val statsRepository = AlarmStatsRepository(sharedPreferences)
        QRSoundingViewModelFactory(statsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_sounding)

        // Inicializar los elementos de la UI
        stopButton = findViewById(R.id.stopButton)
        textViewNombreAlarma = findViewById(R.id.textViewNombreAlarma)
        textViewHoraActual = findViewById(R.id.textViewHoraActual)

        // Obtener el nombre de la alarma desde el Intent y asignarlo al TextView
        val alarmName = intent.getStringExtra("alarm_name")
        textViewNombreAlarma.text = alarmName ?: ""

        // Mostrar la hora actual
        val currentTime = viewModel.getCurrentTime()
        textViewHoraActual.text = currentTime

        // Observa el botón de stop
        stopButton.setOnClickListener {
            iniciarLectorQR() // Lanzar el lector de QR en lugar de parar directamente la alarma
        }

        // Observa shouldFinish para detener la alarma y cerrar la actividad
        viewModel.shouldFinish.observe(this) { shouldFinish ->
            if (shouldFinish == true) {
                AlarmReceiver.stopAlarm() // Detiene la alarma antes de cerrar la actividad
                finish() // Cierra la actividad cuando shouldFinish es true
            }
        }
    }

    // Configuración del lector de QR
    private fun iniciarLectorQR() {
        val options = ScanOptions()
        options.setPrompt("Escanea un código QR")
        options.setBeepEnabled(true)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(true) // Bloquea la orientación en vertical
        options.setCaptureActivity(CustomScannerActivity::class.java)

        qrLauncher.launch(options)
    }

    // Manejar el resultado del lector de QR
    val qrLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            // Comprobamos que lo leído por el QR sea igual a lo que hay en el archivo XML.
            val qrContent = getString(R.string.qr_password)
            val isValid = result.contents == qrContent
            onQrResult(isValid) // Llama al callback con el resultado
        } else {
            onQrResult(false) // Llama al callback con false si el escaneo fue cancelado
        }
    }

    fun onQrResult(isValid: Boolean) {
        if (isValid) {
            Toast.makeText(this, "ALARMA DESACTIVADA!", Toast.LENGTH_LONG).show()
            viewModel.stopAlarm()
        } else {
            Toast.makeText(this, "EL QR NO COINCIDE, PRUEBA OTRA VEZ!", Toast.LENGTH_SHORT).show()
        }
    }
}
