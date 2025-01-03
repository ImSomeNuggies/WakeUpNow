package com.example.myapplication.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.repository.AlarmStatsRepository
import com.example.myapplication.viewmodel.SudokuSoundingViewModel
import com.example.myapplication.viewmodel.factory.SudokuSoundingViewModelFactory
import com.example.myapplication.viewmodel.AlarmReceiver

class SudokuSoundingActivity : AppCompatActivity() {

    private lateinit var textViewNombreAlarma: TextView
    private lateinit var textViewHoraActual: TextView
    private var selectedNumber: String? = null

    // Usamos el ViewModel con un ViewModelFactory que recibe el repositorio
    val viewModel: SudokuSoundingViewModel by viewModels {
        val sharedPreferences = getSharedPreferences("alarm_statistics", MODE_PRIVATE)
        SudokuSoundingViewModelFactory(AlarmStatsRepository(sharedPreferences))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sudoku_sounding)

        // Inicializar los elementos de la UI
        textViewNombreAlarma = findViewById(R.id.textViewNombreAlarma)
        textViewHoraActual = findViewById(R.id.textViewHoraActual)

        val button1 = findViewById<Button>(R.id.button_1)
        val button2 = findViewById<Button>(R.id.button_2)
        val button3 = findViewById<Button>(R.id.button_3)
        val button4 = findViewById<Button>(R.id.button_4)
        val buttonErase = findViewById<Button>(R.id.button_erase)

        val buttons = listOf(button1, button2, button3, button4, buttonErase)

        // Obtener el nombre de la alarma desde el Intent y asignarlo al TextView
        val alarmName = intent.getStringExtra("alarm_name")
        textViewNombreAlarma.text = alarmName ?: ""

        // Mostrar la hora actual
        val currentTime = viewModel.getCurrentTime()
        textViewHoraActual.text = currentTime

        // Asignar manejadores de clics en los botones
        button1.setOnClickListener { selectNumber("1", button1, buttons) }
        button2.setOnClickListener { selectNumber("2", button2, buttons) }
        button3.setOnClickListener { selectNumber("3", button3, buttons) }
        button4.setOnClickListener { selectNumber("4", button4, buttons) }
        buttonErase.setOnClickListener { selectNumber("erase", buttonErase, buttons) }

        // Inicializar el tablero con los números generados
        initializeSudokuGrid()

        // Observa shouldFinish para detener la alarma y cerrar la actividad
        viewModel.shouldFinish.observe(this) { shouldFinish ->
            if (shouldFinish == true) {
                AlarmReceiver.stopAlarm()
                finish()
            }
        }
    }

    private fun selectNumber(number: String, selectedButton: Button, buttons: List<Button>) {
        selectedNumber = number
        buttons.forEach { it.setBackgroundResource(R.drawable.sudoku_button) }
        selectedButton.setBackgroundResource(R.drawable.selected_sudoku_button)
    }

    private fun initializeSudokuGrid() {
        val board = viewModel.getBoard()
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

        cells.forEachIndexed { index, cell ->
            val row = index / 4
            val col = index % 4
            val value = board[row][col]

            if (value != 0) {
                cell.text = value.toString()
                cell.setTextColor(ContextCompat.getColor(this, R.color.black))
            } else {
                cell.text = ""
                cell.setTextColor(ContextCompat.getColor(this, R.color.transparent))
                setupCellClick(cell, row, col)
            }
        }
    }

    // Configura el comportamiento al hacer clic en una celda
    private fun setupCellClick(cell: TextView, row: Int, col: Int) {
        cell.setOnClickListener {
            // Primero verifica si la celda es editable
            if (viewModel.isEditable(row, col)) {
                // Se borra el contenido si se ha seleccionado "Borrar" o aún no se ha seleccionado nada
                if (selectedNumber == "erase" || selectedNumber == null) {
                    cell.text = ""
                    cell.setTextColor(ContextCompat.getColor(this, R.color.transparent))
                } else {
                    // Se comprueba si el valor introducido es correcto
                    val isCorrect = viewModel.checkAndPlaceNumber(row, col, selectedNumber!!.toInt())
                    if (isCorrect) {
                        // Se pone el número lila si es correcto
                        cell.setTextColor(ContextCompat.getColor(this, R.color.lila))
                    } else {
                        // Se pone el número en rojo si es incorrecto
                        cell.setTextColor(ContextCompat.getColor(this, R.color.red))
                    }
                    cell.text = selectedNumber
                }
            }
        }
    }
}
