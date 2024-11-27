package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.viewmodels.SudokuSoundingViewModel
import com.example.myapplication.viewmodels.SudokuSoundingViewModelFactory
import com.example.myapplication.receivers.AlarmReceiver

class SudokuSoundingActivity : AppCompatActivity() {

    lateinit var stopButton: Button
    private lateinit var textViewNombreAlarma: TextView
    private lateinit var textViewHoraActual: TextView
    private var selectedNumber: String? = null

    // Usamos el ViewModel con un ViewModelFactory
    private val viewModel: SudokuSoundingViewModel by viewModels { SudokuSoundingViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sudoku_sounding)

        // Inicializar los elementos de la UI
        stopButton = findViewById(R.id.stopButton)
        textViewNombreAlarma = findViewById(R.id.textViewNombreAlarma)
        textViewHoraActual = findViewById(R.id.textViewHoraActual)

        val button1 = findViewById<Button>(R.id.button_1)
        val button2 = findViewById<Button>(R.id.button_2)
        val button3 = findViewById<Button>(R.id.button_3)
        val button4 = findViewById<Button>(R.id.button_4)
        val buttonErase = findViewById<Button>(R.id.button_erase)

        // Lista de todos los botones para restablecer su apariencia
        val buttons = listOf(button1, button2, button3, button4, buttonErase)

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

        // Asignar manejadores de clics en los botones
        button1.setOnClickListener { selectNumber("1", button1, buttons) }
        button2.setOnClickListener { selectNumber("2", button2, buttons) }
        button3.setOnClickListener { selectNumber("3", button3, buttons) }
        button4.setOnClickListener { selectNumber("4", button4, buttons) }
        buttonErase.setOnClickListener { selectNumber("erase", buttonErase, buttons) }

        setupSudokuGrid()

        // Observa shouldFinish para detener la alarma y cerrar la actividad
        viewModel.shouldFinish.observe(this) { shouldFinish ->
            if (shouldFinish == true) {
                AlarmReceiver.stopAlarm()  // Detiene la alarma antes de cerrar la actividad
                finish()  // Cierra la actividad cuando shouldFinish es true
            }
        }
    }

    private fun selectNumber(number: String, selectedButton: Button, buttons: List<Button>) {
        selectedNumber = number // Actualizar número seleccionado

        // Cambiar el fondo del botón seleccionado y restablecer los demás
        buttons.forEach { it.setBackgroundResource(R.drawable.sudoku_button) }
        selectedButton.setBackgroundResource(R.drawable.selected_sudoku_button)
    }

    private fun setupSudokuGrid() {
        val cells = listOf(
            findViewById<TextView>(R.id.cell_00),
            findViewById<TextView>(R.id.cell_01),
            findViewById<TextView>(R.id.cell_02),
            findViewById<TextView>(R.id.cell_03),
            findViewById<TextView>(R.id.cell_10),
            findViewById<TextView>(R.id.cell_11),
            findViewById<TextView>(R.id.cell_12),
            findViewById<TextView>(R.id.cell_13),
            findViewById<TextView>(R.id.cell_20),
            findViewById<TextView>(R.id.cell_21),
            findViewById<TextView>(R.id.cell_22),
            findViewById<TextView>(R.id.cell_23),
            findViewById<TextView>(R.id.cell_30),
            findViewById<TextView>(R.id.cell_31),
            findViewById<TextView>(R.id.cell_32),
            findViewById<TextView>(R.id.cell_33)
        )

        // Asigna un manejador de clics a cada casilla
        cells.forEach { cell ->
            cell.setOnClickListener {
                // Escribir el número seleccionado o borrar el contenido
                if (selectedNumber == "erase") {
                    cell.text = "" // Borra el contenido si no hay número seleccionado
                } else {
                    cell.text = selectedNumber // Escribe el número seleccionado
                }
            }
        }
    }

}
