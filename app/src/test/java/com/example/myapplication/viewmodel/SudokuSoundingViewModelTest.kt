package com.example.myapplication.viewmodel

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.model.AlarmStatistic
import com.example.myapplication.repository.AlarmStatsRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import java.text.SimpleDateFormat
import java.util.*

class SudokuSoundingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Para LiveData

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var statsRepository: AlarmStatsRepository
    private lateinit var viewModel: SudokuSoundingViewModel

    private val mockTimeProvider: () -> Long = { 1672531200000L } // Fecha fija: 2023-01-01 00:00:00
    private val mockDateFormatter: () -> String = {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dateFormat.format(Date(mockTimeProvider()))
    }

    @Before
    fun setUp() {
        // Mock de SharedPreferences y Editor
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        // Configuración de los mocks
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)

        // Inicializar AlarmStatsRepository con SharedPreferences mockeado
        statsRepository = AlarmStatsRepository(sharedPreferences)

        // Inicializar ViewModel con los mocks
        viewModel = SudokuSoundingViewModel(
            statsRepository = statsRepository,
            timeProvider = mockTimeProvider,
            dateFormatter = mockDateFormatter
        )
    }

    @Test
    fun `currentTime is initialized correctly`() {
        assertEquals("00:00", viewModel.currentTime.value)
    }

    @Test
    fun `setAlarmName updates alarmName correctly`() {
        viewModel.setAlarmName("Morning Alarm")
        assertEquals("Morning Alarm", viewModel.alarmName.value)
    }

    @Test
    fun `stopAlarm saves statistics and sets shouldFinish to true`() {
        viewModel.stopAlarm()

        verify(editor).putString(eq("statistics"), anyString())
        verify(editor).apply()

        assertTrue(viewModel.shouldFinish.value == true)
    }

    @Test
    fun `checkAndPlaceNumber increases failures if incorrect`() {
        val isCorrect = viewModel.checkAndPlaceNumber(0, 0, 5)
        assertFalse(isCorrect)
        assertEquals(1, viewModel.getFailures())
    }

    @Test
    fun `checkAndPlaceNumber places correct number and does not increase failures`() {
        val solution = viewModel.getSolution().clone()

        // Encontrar una celda editable
        var row = -1
        var col = -1
        loop@ for (r in solution.indices) {
            for (c in solution[r].indices) {
                if (viewModel.isEditable(r, c)) {
                    row = r
                    col = c
                    break@loop
                }
            }
        }

        // Verificar que se encontró una celda editable
        assertTrue(row != -1 && col != -1)

        val correctNumber = solution[row][col]

        // Ejecutar la función a probar
        val isCorrect = viewModel.checkAndPlaceNumber(row, col, correctNumber)

        // Verificaciones
        assertTrue(isCorrect) // El número se colocó correctamente
        assertEquals(0, viewModel.getFailures()) // No se incrementaron los fallos
    }

    @Test
    fun `checkAndPlaceNumber completes sudoku and stops alarm`() {
        // Simula completar el Sudoku correctamente
        for (row in 0 until 4) {
            for (col in 0 until 4) {
                if(viewModel.isEditable(row, col)) {
                    val correctNumber = viewModel.getSolution()[row][col]
                    viewModel.checkAndPlaceNumber(row, col, correctNumber)
                }
            }
        }
        assertTrue(viewModel.getShouldFinish() == true)
    }

    @Test
    fun `getBoard returns a valid user board`() {
        val board = viewModel.getBoard()
        assertNotNull(board)
        assertEquals(4, board.size)
        assertEquals(4, board[0].size)
    }

    @Test
    fun `failures increase only on incorrect moves`() {
        val solution = viewModel.getSolution().clone()

        // Encontrar una celda editable
        var row = -1
        var col = -1
        loop@ for (r in solution.indices) {
            for (c in solution[r].indices) {
                if (viewModel.isEditable(r, c)) {
                    row = r
                    col = c
                    break@loop
                }
            }
        }

        // Verificar que se encontró una celda editable
        assertTrue(row != -1 && col != -1)

        // Coloca un número incorrecto en la celda editable
        val incorrectNumber = (solution[row][col] + 1) % 9 + 1 // Número diferente al correcto
        viewModel.checkAndPlaceNumber(row, col, incorrectNumber)
        assertEquals(1, viewModel.getFailures())

        // Coloca el número correcto en la misma celda
        val correctNumber = solution[row][col]
        viewModel.checkAndPlaceNumber(row, col, correctNumber)
        assertEquals(1, viewModel.getFailures()) // No debe incrementarse
    }


    @Test
    fun `stopAlarm does not overwrite statistics if already stopped`() {
        viewModel.stopAlarm()
        val initialShouldFinish = viewModel.shouldFinish.value
        val initialFailures = viewModel.getFailures()

        // Llama a stopAlarm nuevamente
        viewModel.stopAlarm()

        // Asegúrate de que las estadísticas no cambian
        assertEquals(initialShouldFinish, viewModel.shouldFinish.value)
        assertEquals(initialFailures, viewModel.getFailures())
    }
}