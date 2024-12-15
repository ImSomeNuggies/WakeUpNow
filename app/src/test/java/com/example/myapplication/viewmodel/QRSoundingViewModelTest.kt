package com.example.myapplication.viewmodel

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.model.AlarmStatistic
import com.example.myapplication.repository.AlarmStatsRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import java.text.SimpleDateFormat
import java.util.*

class QRSoundingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Para permitir pruebas con LiveData

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var statsRepository: AlarmStatsRepository
    private lateinit var viewModel: QRSoundingViewModel

    private val mockTimeProvider: () -> Long = { 1672531200000L } // Fecha fija: 2023-01-01 00:00:00 UTC
    private val mockDateFormatter: () -> String = {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC") // Configura zona horaria a UTC
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

        // Inicializar el repositorio con SharedPreferences mockeado
        statsRepository = AlarmStatsRepository(sharedPreferences)

        // Inicializar el ViewModel con el repositorio y mocks de tiempo
        viewModel = QRSoundingViewModel(
            statsRepository = statsRepository,
            timeProvider = mockTimeProvider,
            dateFormatter = mockDateFormatter
        )
    }

    @Test
    fun `currentTime is initialized correctly`() {
        assertEquals("00:00", viewModel.currentTime.value) // Basado en el mock del dateFormatter
    }

    @Test
    fun `setAlarmName updates alarmName LiveData correctly`() {
        viewModel.setAlarmName("Test Alarm")
        assertEquals("Test Alarm", viewModel.alarmName.value)
    }

    @Test
    fun `stopAlarm saves statistics and updates shouldFinish`() {
        // Llamar a stopAlarm
        viewModel.stopAlarm()

        // Verificar que se guarda una estadística en SharedPreferences
        verify(editor).putString(eq("statistics"), anyString())
        verify(editor).apply()

        // Verificar que shouldFinish se actualiza a true
        assertTrue(viewModel.shouldFinish.value == true)
    }
}